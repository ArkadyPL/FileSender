package com.filesender;

import com.filesender.Cryptography.AES;
import com.filesender.HelperClasses.*;
import com.filesender.Cryptography.RSA;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Connection {

   public static void connectToRemote(){
        if(globals.connectButton.getText().equals("Connect")) {
            if (globals.remoteIPTextField.getText().equals("") || globals.remotePinTextField.getText().equals("")) {
                Log.Write("Error: remote IP and PIN cannot be empty!");
                return;
            }

            Boolean isValid = new IPAddressValidator().validate(globals.remoteIPTextField.getText());
            Log.Write("Connection button clicked. Remote IP value: " + globals.remoteIPTextField.getText() + "\tGiven IP address is " + (isValid ? "valid" : "not valid"));
            if (isValid) {
                AppState.changeToConnected();
                try {
                    globals.remoteIP = InetAddress.getByName(globals.remoteIPTextField.getText());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                try {
                    Log.Write("Trying to connect to remote server...");
                    globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                } catch (ConnectException e) {
                    Log.Write("Remote is not available...");
                    AppState.changeToDisconnected();

                } catch (Exception e) {
                    Log.Write("Connection error...");
                    AppState.changeToDisconnected();
                }

                if (globals.connectionSocket != null) {
                    Log.Write("Connected to remote!");
                    try {
                        boolean result = Connection.exchangeKeysClient();
                        if (!result) {//if something went wrong (e.g. pin incorrect, remote busy)
                            AppState.changeToDisconnected();
                            return;
                        }
                    } catch (IOException e) {
                        AppState.changeToDisconnected();
                        e.printStackTrace();
                    }

                    try {
                        globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                    } catch (IOException e) {
                        Log.Write("Something went wrong setting up a new socket after key exchange...");
                        AppState.changeToDisconnected();
                        e.printStackTrace();
                    }

                    try {
                        Receiver.receiveTree(globals.remoteTree, globals.connectionSocket, "root", false);
                    } catch (IOException | ClassNotFoundException e) {
                        Log.Write("Something went wrong while downloading the File Tree");
                        AppState.changeToDisconnected();
                        e.printStackTrace();
                    }
                    globals.frame.repaint();
                    globals.frame.revalidate();
                }
            }
        }
        else if(globals.connectButton.getText().equals("Disconnect")){
            AppState.changeToDisconnected();
        }
    }

    //after our request of connection
    public static boolean exchangeKeysClient() throws IOException {
        //Send our public key
        ObjectOutputStream ostream = new ObjectOutputStream(globals.connectionSocket.getOutputStream());
        Operation basicOperation = new Operation(5, null,null, RSA.pubKey);
        ostream.writeObject(basicOperation);

        //Receive their public key
        ObjectInputStream inFromServer = new ObjectInputStream(globals.connectionSocket.getInputStream());
        try { RSA.remoteKey = (RSAPublicKey) inFromServer.readObject(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        if(RSA.remoteKey == null){
            Log.Write("Your connection request was rejected: Remote is busy");
            return false;
        }

        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(RSA.remoteKey.getEncoded()));

        //Send encrypted PIN
        ostream.writeObject(RSA.encrypt(globals.remotePinTextField.getText()));

        //Receive info if pin was ok. If it wasn't ok shut down the connection
        Operation response = null;
        try { response = (Operation)inFromServer.readObject(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        String result = (String) RSA.decrypt(response.argument1Encrypted);

        if( !result.equals("OK") ){//If pin was not correct
            Log.Write("Connection finished: wrong PIN value");
            return false;
        }
        //If pin was ok read server's symmetricKey
        AES.symmetricKey = (SecretKey) RSA.decrypt(response.obj1Encrypted);
        Log.WriteTerminal("New SymmetricKey: " + DatatypeConverter.printHexBinary(AES.symmetricKey.getEncoded()));
        Log.Write("Connection set up properly!");
        return true;
    }

    //after someone requests us a connection
    public static void exchangeKeysServer(Operation basicOp, Socket connectedSocket, ObjectInputStream inFromServer) throws IOException, ClassNotFoundException {
        Log.Write("Setting up the connection...");
        //Receive and save remote public key
        RSA.remoteKey = (RSAPublicKey)basicOp.obj1;
        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(RSA.remoteKey.getEncoded()));

        ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
        ostream.writeObject(RSA.pubKey);

        //Receive pin and compare to real value
        String tryPin = (String)RSA.decrypt((byte[])inFromServer.readObject());
        Operation message = new Operation(6, null, null, null);
        //We encrypt the message, we cannot use Operation.encryptFields() bacause it uses AES and we don't have AES Key set yet,
        //We have to encrypt fields separately
        if( !tryPin.equals(globals.localPIN) ){
            //Pin was wrong so we only send info message
            message.argument1Encrypted = RSA.encrypt("WRONG_PIN");
            //Below we encrypt some random stuff so nobody knows if pin was ok or not. Otherwise this field would be null which is synonymous with wrong pin info
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            Date date = new Date();
            message.obj1Encrypted = RSA.encrypt(dateFormat.format(date));
            ostream.writeObject(message);

            Log.Write("Connection finished: wrong pin value!");
        }else {
            //pin was alright so we send inform message together with our symmetric key
            Log.WriteTerminal("Pin ok. Sharing symmetric key...");
            message.argument1Encrypted = RSA.encrypt("OK");
            //new symmetric key for new client, we lost old one but assumption is that there is only one client at the time (to be implemented)
            AES.generateSymmetricKey();
            message.obj1Encrypted = RSA.encrypt(AES.symmetricKey);
            ostream.writeObject(message);

            Log.Write("Connection set up properly!");
        }


    }
}
