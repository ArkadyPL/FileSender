package com.filesender.HelperClasses;

import javax.swing.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

/**
 * Created by Piotr on 15.01.2017.
 */
public class globals {
    public static JFrame frame = new JFrame("File Sender");
    public static Object previousDir = null;
    public static Stack dirStack = new Stack();
    public static String localIP = null;
    public static InetAddress remoteIP = null;
    public static volatile boolean isConnected = false;
    public static Socket connectionSocket = null;
    public static ServerSocket serverSocket = null;
    public static JTree localTree = new JTree();
    public static JTree remoteTree = new JTree();
}