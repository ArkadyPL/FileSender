package com.filesender;

import com.filesender.HelperClasses.*;
import com.filesender.Cryptography.RSA;

import javax.crypto.KeyGenerator;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

public class Connection {

    public static void exchangeKeys() throws IOException {
        //Send our public key
        ObjectOutputStream ostream = new ObjectOutputStream(globals.connectionSocket.getOutputStream());
        Operation basicOperation = new Operation(5, null,null, globals.pubKey);
        ostream.writeObject(basicOperation);

        //Receive their public key
        ObjectInputStream inFromServer = new ObjectInputStream(globals.connectionSocket.getInputStream());
        try {
            globals.remoteKey = (RSAPublicKey) inFromServer.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));

        //Create and send them our common symmetric key
        KeyGenerator KeyGen = null;
        try {
            KeyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyGen.init(128);
        globals.symmetricKey = KeyGen.generateKey();
        Log.WriteTerminal("SymmetricKey:\n" + DatatypeConverter.printHexBinary(globals.symmetricKey.getEncoded()));
        ostream.writeObject(RSA.encrypt(globals.symmetricKey));

    }

    public static void connectToRemote(JTextField remoteIPTextField){
        try {
            globals.remoteIP = InetAddress.getByName(remoteIPTextField.getText());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Boolean isValid = new IPAddressValidator().validate(remoteIPTextField.getText());
        Log.WriteTerminal("Connection button clicked. Remote IP value: " + globals.remoteIP  + "\tGiven IP address is " + (isValid ? "valid" : "not valid"));
        if(isValid) {
            try {
                Log.Write("Trying to connect to remote server...");
                globals.connectionSocket = new Socket(globals.remoteIP, 9990);
            } catch (ConnectException e) {
                Log.Write("Remote is not available...");
            } catch (Exception e) {
                Log.Write("Connection error...");
            }
            if (globals.connectionSocket != null) {
                globals.isConnected = true;
                ServerStatus connectionStatusChecker = new ServerStatus();
                connectionStatusChecker.setDaemon(true);
                connectionStatusChecker.start();
                Log.Write("Connected to remote!");
                try {
                    Connection.exchangeKeys();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Receiver.receiveTree(globals.remoteTree,globals.connectionSocket,"root",false);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                globals.frame.repaint();
                globals.frame.revalidate();
            }
        }
    }
}
