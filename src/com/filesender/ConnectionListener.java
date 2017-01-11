package com.filesender;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.filesender.FileSender.isConnected;

/**
 * Created by arkadiusz.ryszewski on 10.01.2017.
 */
public class ConnectionListener {
        public static Socket ListenForIncomingConnections() throws IOException {
            Socket connectedSocket;
            ServerSocket serverSocket = new ServerSocket(9990);

            System.out.println("Waiting for incoming connections...");
            connectedSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            if(!isConnected){
                Connectioner.ConnectToClient(connectedSocket);
            }
            return connectedSocket;
        }
}