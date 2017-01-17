package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.HelperClasses.globals;

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
            Sender.sendTree(connectedSocket,localTreeModel,serverSocket,basicOp);
        }
        else if(basicOp.opID == 2) {//Send given file
            Sender.sendFile(basicOp.argument1,localTreeModel,connectedSocket,serverSocket);
        }else if(basicOp.opID == 5){//save new public key from arg1 and send your public key in arg1
            globals.remoteKey = (RSAPublicKey)basicOp.obj1;
            Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));
            ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
            Operation basicOperation = new Operation(6, null,null, globals.pubKey);
            ostream.writeObject(basicOperation);

            ConnectionListener.ListenForIncomingConnections(localTreeModel,serverSocket);
        }else if(basicOp.opID == 6){//save new public key from arg1
            globals.remoteKey = (RSAPublicKey)basicOp.obj1;
            Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));

            ConnectionListener.ListenForIncomingConnections(localTreeModel,serverSocket);
        }

        return connectedSocket;
    }
}