package com.filesender;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.*;
import java.io.*;
/**
 * Created by Arek on 20.12.2016.
 */
public class FileSender {
    public static void main(String[] args) {
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ip = null; //you get the IP as a String
        try {
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ip);

        JFrame frame = new JFrame("File Sender");
        frame.setSize( 800, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        JLabel IPlabel = new JLabel("Hello! Your IP is : " + ip);
        JPanel panel = new JPanel();
        panel.add(IPlabel);
        frame.add(panel);
        frame.setVisible(true);
    }
    //Komentarz testowy
}