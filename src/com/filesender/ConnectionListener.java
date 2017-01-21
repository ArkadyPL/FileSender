package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.Cryptography.RSA;
import com.filesender.HelperClasses.globals;

import javax.crypto.SecretKey;
import javax.swing.tree.TreeModel;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.interfaces.RSAPublicKey;


public class ConnectionListener {
    public static Socket ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        Log.Write("Waiting for incoming connections...");

        connectedSocket = serverSocket.accept();

        Log.Write("Connection accepted!");

        ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
        Operation basicOp;
        basicOp = (Operation)inFromServer.readObject();

        if(basicOp.opID == 1) {//Rebuild tree for argument as a root
            Log.WriteTerminal("Operation ID 1 executed");
            basicOp.decryptFields();
            Sender.sendTree(connectedSocket,localTreeModel,serverSocket,basicOp);
        }
        else if(basicOp.opID == 2) {//Send given file
            basicOp.decryptFields();
            Sender.sendFile(basicOp.argument1,localTreeModel,connectedSocket,serverSocket);

        }else if(basicOp.opID == 5){//save new public key from arg1 and send your public key in arg1
            Log.Write("Setting up the connection...");
            //Receive and save remote public key
            globals.remoteKey = (RSAPublicKey)basicOp.obj1;
            Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));

            ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
            ostream.writeObject(globals.pubKey);

            //Receive pin and compare to real value
            String tryPin = (String)RSA.decrypt((byte[])inFromServer.readObject());
            if( !tryPin.equals(globals.PIN) ){
                ostream.writeObject(RSA.encrypt("WRONG_PIN"));
                globals.remoteKey = null;
                Log.Write("Connection finished: wrong pin value!");
            }else {//if not wrong, proceed
                ostream.writeObject(RSA.encrypt("OK"));

                globals.symmetricKey = (SecretKey) RSA.decrypt((byte[]) inFromServer.readObject());
                Log.WriteTerminal("SymmetricKey:\n" + DatatypeConverter.printHexBinary(globals.symmetricKey.getEncoded()));
                Log.Write("Connection set up properly!");
            }

            ConnectionListener.ListenForIncomingConnections(localTreeModel, serverSocket);
        }

        return connectedSocket;
    }
}