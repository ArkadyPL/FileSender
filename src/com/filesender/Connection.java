package com.filesender;

import com.filesender.Cryptography.AES;
import com.filesender.HelperClasses.*;
import com.filesender.Cryptography.RSA;

import javax.crypto.SecretKey;
import javax.swing.tree.TreeModel;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class containing static methods for setting up the connection.
 */
public class Connection {

    /**
     * Static method for listening and handling of all incoming requests.
     * Should be called in while(true) loop in the end of {@link FileSender#main(String[])} app function.
     * @param localTreeModel Our local file tree that we may have to send if requested.
     * @param serverSocket Server socket for listening. Should be the same for the all calls of the method. Port: 9990.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        Log.Write("Waiting for incoming requests...");

        connectedSocket = serverSocket.accept();
        InetSocketAddress tempIP = (InetSocketAddress)connectedSocket.getRemoteSocketAddress();
        globals.remoteIP = connectedSocket.getInetAddress();
        //Connect only if we are not connected to anyone or if request is coming from the remote that we are connected to
        if(globals.remoteIP == null || globals.remoteIP.equals(tempIP.getAddress())) {
            Log.Write("Connection accepted!");

            ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
            Operation basicOp;
            basicOp = (Operation) inFromServer.readObject();

            if (basicOp.opID == 1) {//Rebuild tree for argument as a root
                basicOp.decryptFields();
                Log.Write("Local file tree requested");
                Sender.sendTree(connectedSocket, localTreeModel, basicOp);
            }
            else if (basicOp.opID == 2) {//Send requested file
                basicOp.decryptFields();
                Log.Write("File " + basicOp.argument1 + " requested");
                Sender.sendFile(basicOp.argument1, connectedSocket);
            }
            else if (basicOp.opID == 5) {//exchange keys server side
                Connection.exchangeKeysServer(basicOp, connectedSocket, inFromServer);
            }
            else if (basicOp.opID == 6) {//receive file
                basicOp.decryptFields();
                Receiver.receiveFile(connectedSocket,basicOp.obj1);
            }
        }else{
            Log.Write("Incoming request from another source rejected! We are busy...");
            ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
            ostream.writeObject(null);
        }
    }

    /**
     * Static method that is called when use wishes to connected remote server.
     */
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
                        Log.Write("Something went wrong while exchanging the cryptographic keys");
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
                    ServerStatus connectionStatusChecker = new ServerStatus();
                    connectionStatusChecker.setDaemon(true);
                    connectionStatusChecker.start();
                    globals.frame.repaint();
                    globals.frame.revalidate();
                }
            }
        }
        else if(globals.connectButton.getText().equals("Disconnect")){
            AppState.changeToDisconnected();
        }
    }

    /**
     * Static method realizing full process of exchanging cryptographic keys.
     * @return Returns 'true' if everything is alright and 'false' in case of error.
     * @throws IOException
     */
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

    /**
     * Static method called on server side just after someone requested the connection.
     * @param basicOp Object of type Operation containing all required for connection information.
     * @param connectedSocket Socket realizing the connection.
     * @param inFromServer Input stream to the socket.
     * @throws IOException
     * @throws ClassNotFoundException
     */
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
