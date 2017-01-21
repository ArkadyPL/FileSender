package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.HelperClasses.globals;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectionListener {

    public static Socket ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        Log.Write("Waiting for incoming connections...");

        connectedSocket = serverSocket.accept();
        String tempIP = connectedSocket.getRemoteSocketAddress().toString();

        //Don't let anyone else connect us if we're connected to someone to avoid SymmetricKey leaking
        if(globals.remoteIP == null || globals.remoteIP.toString().equals(tempIP)) {
            Log.Write("Connection accepted!");

            ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
            Operation basicOp;
            basicOp = (Operation)inFromServer.readObject();

            if (basicOp.opID == 1) {//Rebuild tree for argument as a root
                Log.WriteTerminal("Operation ID 1 executed");
                basicOp.decryptFields();
                Sender.sendTree(connectedSocket, localTreeModel, serverSocket, basicOp);
            } else if (basicOp.opID == 2) {//Send requested file
                basicOp.decryptFields();
                Sender.sendFile(basicOp.argument1, localTreeModel, connectedSocket, serverSocket);
            } else if (basicOp.opID == 5) {//exchange keys server side
                Connection.exchangeKeysServer(basicOp, connectedSocket, inFromServer);
                ConnectionListener.ListenForIncomingConnections(localTreeModel, serverSocket);
            }
        }else{
            Log.Write("Incoming connection rejected!");
            ConnectionListener.ListenForIncomingConnections(localTreeModel, serverSocket);
        }

        return connectedSocket;
    }

}