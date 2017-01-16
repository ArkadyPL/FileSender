package com.filesender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by arkadiusz.ryszewski on 10.01.2017.
 */
public class Connectioner {
    public static void ConnectToClient(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
        String message = in.readLine();
        System.out.println("Message is: " + message);
        return;
    }

    public static void ConnectToServer(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("Hello my dear friend!");
        return;
    }
}