package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectionListener {

    public static void ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        Log.Write("Waiting for incoming connections...");

        connectedSocket = serverSocket.accept();
        String tempIP = connectedSocket.getRemoteSocketAddress().toString();

        Log.Write("Connection accepted!");

        ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
        Operation basicOp;
        basicOp = (Operation)inFromServer.readObject();

        if (basicOp.opID == 1) {//Rebuild tree for argument as a root
            basicOp.decryptFields();
            Sender.sendTree(connectedSocket, localTreeModel, basicOp);
        } else if (basicOp.opID == 2) {//Send requested file
            basicOp.decryptFields();
            Sender.sendFile(basicOp.argument1, connectedSocket);
        } else if (basicOp.opID == 5) {//exchange keys server side
            Connection.exchangeKeysServer(basicOp, connectedSocket, inFromServer);
        }
    }

}