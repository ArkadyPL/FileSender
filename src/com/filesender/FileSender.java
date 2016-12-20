package com.filesender;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Arek on 20.12.2016.
 */
public class FileSender {
    public static void main(String[] args) {
        String MyIP = null;
        try {
            MyIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Hello! Your IP is : " + MyIP );
        JFrame frame = new JFrame("File Sender");
        frame.setSize( 800, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        frame.setVisible(true);
    }
}