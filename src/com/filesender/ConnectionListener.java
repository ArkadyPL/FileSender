package com.filesender;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static com.filesender.FileSender.isConnected;
//   ID LIST
/*  0 - no task, just connection check
    1 - Rebuild tree for argument as a root
    2 - Send given file
    3 - Rename given file
 */
class operation implements java.io.Serializable {
    int opID;
    String argument1;
    String argument2;
    public operation(int _ID, String arg1,String arg2) {
        opID = _ID;
        argument1 = arg1;
        argument2 = arg2;
    }
}

public class ConnectionListener {
    public static Socket ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        System.out.println("Waiting for incoming connections...");
        connectedSocket = serverSocket.accept();
        System.out.println("Connection accepted-!");
        ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
        operation basicOp;
        basicOp = (operation)inFromServer.readObject();

        while(true) {
            if(basicOp.opID == 1) {
                System.out.println("Operation ID 1 executed");
                Sender.sendTree(connectedSocket,localTreeModel,serverSocket);
                break;
            }
            else if(basicOp.opID == 2) {
                //todo: Send file
            }
        }
        return connectedSocket;
    }
}
