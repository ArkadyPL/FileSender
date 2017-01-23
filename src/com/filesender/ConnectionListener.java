package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.HelperClasses.globals;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectionListener {

    public static void ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        Log.Write("Waiting for incoming requests...");

        connectedSocket = serverSocket.accept();
        InetSocketAddress tempIP = (InetSocketAddress)connectedSocket.getRemoteSocketAddress();

        if(globals.remoteIP == null || globals.remoteIP.equals(tempIP.getAddress())) {
            Log.Write("Connection accepted!");

            ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
            Operation basicOp;
            basicOp = (Operation) inFromServer.readObject();

            if (basicOp.opID == 1) {//Rebuild tree for argument as a root
                basicOp.decryptFields();
                Log.Write("Local file tree requested");
                Sender.sendTree(connectedSocket, localTreeModel, basicOp);
            } else if (basicOp.opID == 2) {//Send requested file
                basicOp.decryptFields();
                Log.Write("File " + basicOp.argument1 + " requested");
                Sender.sendFile(basicOp.argument1, connectedSocket);
            } else if (basicOp.opID == 5) {//exchange keys server side
                Connection.exchangeKeysServer(basicOp, connectedSocket, inFromServer);
            }
        }else{
            Log.Write("Incoming request from another source rejected! We are busy...");
            ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
            ostream.writeObject(null);
        }
    }

}