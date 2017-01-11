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
        public static Socket ListenForIncomingConnections(TreeModel localTreeModel, JTree remoteTree, JFrame frame, InetAddress _adr) throws IOException, ClassNotFoundException {
            Socket connectedSocket;
            ServerSocket serverSocket = new ServerSocket(9990);

            System.out.println("Waiting for incoming connections...");
            connectedSocket = serverSocket.accept();
            System.out.println("Connection accepted-!");

            if(!isConnected){
                System.out.println("DICK");
                //Connectioner.ConnectToClient(connectedSocket);
                ObjectOutputStream outToClient = new ObjectOutputStream(connectedSocket.getOutputStream());
                Sender.sendTree(outToClient,localTreeModel);
            }
            else {
                connectedSocket = new Socket(_adr, 9990);
                System.out.println("Pussy");
                Receiver.work(remoteTree,frame,connectedSocket);
            }
            return connectedSocket;
        }
}