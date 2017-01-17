package com.filesender.HelperClasses;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Stack;

public class globals {
    public static Cipher cipher = null;
    public static ServerSocket statusSocket = null;
    public static JFrame frame = new JFrame("File Sender");
    public static Object previousDir = null;
    public static Stack dirStack = new Stack();
    public static String localIP = null;
    public static InetAddress remoteIP = null;
    public static Socket connectionSocket = null;
    public static ServerSocket serverSocket = null;
    public static JTree localTree = new JTree();
    public static JTree remoteTree = new JTree();
    public static JTextArea logTextArea = new JTextArea();;
    public static RSAPublicKey pubKey = null;
    public static RSAPrivateKey privKey = null;
    public static RSAPublicKey remoteKey = null;
    public static SecretKey symmetricKey = null;
    public static Cipher aesCipher = null;
    public static volatile boolean isConnected = false;
}