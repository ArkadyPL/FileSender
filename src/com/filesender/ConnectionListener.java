package com.filesender;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static com.filesender.FileSender.isConnected;

/**
 * Created by arkadiusz.ryszewski on 10.01.2017.
 */
public class ConnectionListener {
    public static Socket ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket connectedSocket;
        System.out.println("Waiting for incoming connections...");
        connectedSocket = serverSocket.accept();
        System.out.println("Connection accepted-!");
        Sender.sendTree(connectedSocket,localTreeModel,serverSocket);
        return connectedSocket;
    }
}
