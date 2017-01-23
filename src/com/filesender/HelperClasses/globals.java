package com.filesender.HelperClasses;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Stack;

/**
 * Class containing all the properties that have global usage and are used too often to put them in the local scope.
 * It also contains some methods of general purpose. Its name is written with small case letters to highlight special
 * destiny of this class.
 */
public class globals {
    /**
     * Whole program's frame.
     */
    public static JFrame frame = new JFrame("File Sender");

    /**
     * Previous directory address in remote tree model.
     */
    public static Object previousDir = null;

    /**
     * Internal variable, 'directory stack' for Remote Tree construction.
     */
    public static Stack dirStack = new Stack();

    /**
     * IP of local machine.
     */
    public static String localIP = null;

    /**
     * IP of the server that we are connected to as a client.
     */
    public static InetAddress remoteIP = null;

    /**
     * Socket with connection to the client that has connected to us.
     * Value of this variable is volatile and is changing after each new request.
     */
    public static Socket connectionSocket = null;

    /**
     * Local JTree for displaying local tree model.
     */
    public static JTree localTree = new JTree();

    /**
     * Remote JTree for displaying remote tree model.
     */
    public static JTree remoteTree = new JTree();

    /**
     * Variable used to check connection status.
     */
    public static volatile boolean isConnected = false;

    /**
     * Password that is required if someone wants to connect to us.
     */
    public static String localPIN = generatePIN();



    /**
     * Static method converting object of any class inheriting by Object to byte[] object.
     * @param object Object to be converted to byte[] form.
     * @return Converted object in form of byte[].
     */
    public static byte[] toByte(Object object){
        byte[] result = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutput out = new ObjectOutputStream(bos);
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

    /**
     * Static method converting object in byte[] form to Object. Result needs to be casted to desired class.
     * @param encryptedObject byte[] object to be transformed to Object type.
     * @return Converted object of type Object. Needs to be casted to desired class.
     */
    public static Object toObject(byte[] encryptedObject){
        ByteArrayInputStream bis = new ByteArrayInputStream(encryptedObject);
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

    /**
     * Static method generating random 4-digit pin.
     * @return Random PIN code.
     */
    public static String generatePIN(){
        globals.localPIN = "";
        for(int i=0; i<4; i++){
            //Get one random digit and add it to PIN
            globals.localPIN += String.valueOf(Math.random()*10).substring(0,1);
        }
        Log.Write("Generated PIN is " + globals.localPIN);
        return globals.localPIN;
    }
}