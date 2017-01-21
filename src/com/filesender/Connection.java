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

public class Connection {

   public static void connectToRemote(JTextField remoteIPTextField, JTextField remotePinTF){
        if(remoteIPTextField.getText().equals("") || remotePinTF.getText().equals("")) {
            Log.Write("Error: remote IP and PIN cannot be empty!");
            return;
        }


        Boolean isValid = new IPAddressValidator().validate(remoteIPTextField.getText());
        Log.Write("Connection button clicked. Remote IP value: " + remoteIPTextField.getText()  + "\tGiven IP address is " + (isValid ? "valid" : "not valid"));
        if(isValid) {
            try { globals.remoteIP = InetAddress.getByName(remoteIPTextField.getText()); }
            catch (UnknownHostException e) { e.printStackTrace(); }

            try {
                Log.Write("Trying to connect to remote server...");
                globals.connectionSocket = new Socket(globals.remoteIP, 9990);
            }
            catch (ConnectException e) { Log.Write("Remote is not available..."); }
            catch (Exception e) { Log.Write("Connection error..."); }

            if (globals.connectionSocket != null) {
                globals.isConnected = true;
                ServerStatus connectionStatusChecker = new ServerStatus();
                connectionStatusChecker.setDaemon(true);
                connectionStatusChecker.start();
                Log.Write("Connected to remote!");
                try {
                    boolean result = Connection.exchangeKeysClient(remotePinTF.getText());
                    if( !result ) return;//if something went wrong (e.g. pin incorrect)
                } catch (IOException e) { e.printStackTrace(); }

                try { globals.connectionSocket = new Socket(globals.remoteIP, 9990); }
                catch (IOException e) { e.printStackTrace(); }

                try { Receiver.receiveTree(globals.remoteTree,globals.connectionSocket,"root",false); }
                catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
                globals.frame.repaint();
                globals.frame.revalidate();
            }
        }
    }

    //after our request of connection
    public static boolean exchangeKeysClient(String remotePin) throws IOException {
        //Send our public key
        ObjectOutputStream ostream = new ObjectOutputStream(globals.connectionSocket.getOutputStream());
        Operation basicOperation = new Operation(5, null,null, RSA.pubKey);
        ostream.writeObject(basicOperation);

        //Receive their public key
        ObjectInputStream inFromServer = new ObjectInputStream(globals.connectionSocket.getInputStream());
        try { RSA.remoteKey = (RSAPublicKey) inFromServer.readObject(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(RSA.remoteKey.getEncoded()));

        //Send encrypted PIN
        ostream.writeObject(RSA.encrypt(remotePin));

        //Receive info if pin was ok. If it wasn't ok shut down the connection
        String result = null;
        try { result = (String)RSA.decrypt((byte[])inFromServer.readObject()); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        if(!result.equals("OK")){//If pin was not correct
            globals.remoteIP = null;
            RSA.remoteKey = null;
            Log.Write("Connection finished: wrong PIN value");
            return false;
        }

        //Receive server's summetric key

        //Send them our symmetric key
        ostream.writeObject(RSA.encrypt(AES.symmetricKey));
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
        if( !tryPin.equals(globals.localPIN) ){
            ostream.writeObject(RSA.encrypt("WRONG_PIN"));

            Log.Write("Connection finished: wrong pin value!");
        }else {//if not wrong, proceed
            ostream.writeObject(RSA.encrypt("OK"));


            AES.symmetricKey = (SecretKey) RSA.decrypt((byte[]) inFromServer.readObject());
            Log.WriteTerminal("SymmetricKey:\n" + DatatypeConverter.printHexBinary(AES.symmetricKey.getEncoded()));
            Log.Write("Connection set up properly!");
        }


    }
}
