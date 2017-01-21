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
    public static Cipher aesCipher = null;
    public static volatile boolean isConnected = false;
    public static String PIN = null;

    public static byte[] toByte(Object object){
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

    public static Object toObject(byte[] enbytedObject){
        ByteArrayInputStream bis = new ByteArrayInputStream(enbytedObject);
        ObjectInput in = null;
        Object result = null;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
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

    public static String generatePIN(){
        globals.PIN = "";
        for(int i=0; i<4; i++){
            //Get one random digit and add it to PIN
            globals.PIN += String.valueOf(Math.random()*10).substring(0,1);
        }
        Log.Write("Generated PIN is " + globals.PIN);
        return globals.PIN;
    }
}