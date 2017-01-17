package com.filesender.HelperClasses;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.*;
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

    public static byte[] ToByte(Object object){
        byte[] result = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return result;
    }

    public static Object ToObject(byte[] enbytedObject){
        ByteArrayInputStream bis = new ByteArrayInputStream(enbytedObject);
        ObjectInput in = null;
        Object result = null;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return result;
    }
}