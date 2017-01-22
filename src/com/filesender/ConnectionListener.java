package com.filesender;

import com.filesender.Cryptography.RSA;
import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.HelperClasses.globals;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectionListener {

    public static void ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        Log.Write("Waiting for incoming connections...");

        connectedSocket = serverSocket.accept();
        InetSocketAddress tempIP = (InetSocketAddress)connectedSocket.getRemoteSocketAddress();

        if(globals.remoteIP == null || globals.remoteIP.equals(tempIP.getAddress())) {
            Log.Write("Connection accepted!");

            ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
            Operation basicOp;
            basicOp = (Operation) inFromServer.readObject();

            if (basicOp.opID == 1) {//Rebuild tree for argument as a root
                basicOp.decryptFields();
                Sender.sendTree(connectedSocket, localTreeModel, basicOp);
            } else if (basicOp.opID == 2) {//Send requested file
                basicOp.decryptFields();
                Sender.sendFile(basicOp.argument1, connectedSocket);
            } else if (basicOp.opID == 5) {//exchange keys server side
                Connection.exchangeKeysServer(basicOp, connectedSocket, inFromServer);
            }
        }else{
            Log.Write("Connection rejected!");
            ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
            ostream.writeObject(null);
        }
    }

}