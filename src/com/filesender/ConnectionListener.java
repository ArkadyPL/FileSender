package com.filesender;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.filesender.FileSender.isConnected;

/**
 * Created by arkadiusz.ryszewski on 10.01.2017.
 */
public class ConnectionListener {
        public static Socket ListenForIncomingConnections(TreeModel localTreeModel) throws IOException {
            Socket connectedSocket;
            ServerSocket serverSocket = new ServerSocket(9990);

            System.out.println("Waiting for incoming connections...");
            connectedSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            ObjectOutputStream outToClient = new ObjectOutputStream(connectedSocket.getOutputStream());
            Sender.sendTree(outToClient,localTreeModel);
            if(!isConnected){
                Connectioner.ConnectToClient(connectedSocket);
            }
            return connectedSocket;
        }
}