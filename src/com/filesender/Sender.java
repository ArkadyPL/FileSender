package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.swing.tree.TreeModel;
class to_send implements java.io.Serializable {
    Object node;
    Boolean isRoot;
    public to_send(Object _node, Boolean is) {
        node = _node;
        isRoot = is;
    }
}

public class Sender extends  Thread {
    static OutputStream out = null;
    static Queue queue = new LinkedList();

    public static void sendTree(Socket connectedSocket, TreeModel localTreeModel, ServerSocket servSock, operation rooot) throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
        File rootObj =  null;
        if(Objects.equals(rooot.argument1, "root")) {
            rootObj = new File(localTreeModel.getRoot().toString());
        }
        else {
            rootObj = new File(rooot.obj1.toString());
        }
        to_send ts = new to_send(rootObj,true);
        ostream.writeObject(ts);
        Log.Write("Sending file tree...");
        for(int i = 0; i < localTreeModel.getChildCount(rootObj);i++) {
            if( localTreeModel.isLeaf(localTreeModel.getChild(rootObj,i)) ) {
                to_send ts2 = new to_send(localTreeModel.getChild(rootObj,i),false);
                Log.WriteTerminal("sending: " + ts2.node);
                ostream.writeObject(ts2);
                queue.add(localTreeModel.getChild(rootObj,i));
            }
            else {
                to_send ts2 = new to_send(localTreeModel.getChild(rootObj,i),true);
                Log.WriteTerminal("sending: " + ts2.node);
                ostream.writeObject(ts2);
                queue.add(localTreeModel.getChild(rootObj,i));
            }
        }
        ostream.close();
        Log.WriteTerminal("New tree sent");
        connectedSocket = ConnectionListener.ListenForIncomingConnections(localTreeModel,servSock);
    }

    public static int sendFile(String current_file,  TreeModel localTreeModel, Socket socket,ServerSocket servSock) throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Log.Write("File to send: " + current_file);
        File myFile = new File(current_file);
        byte[] buffer = new byte[(int) myFile.length()];
        out = socket.getOutputStream();
        FileInputStream fis = null;
        try {
             fis = new FileInputStream(myFile);
        }
        catch (FileNotFoundException e) {
            operation op1 = new operation(1,current_file,null,current_file);
            Sender.sendTree(socket,localTreeModel,servSock,op1);
        }
        out = socket.getOutputStream();
        Log.Write("Sending files");

        Cipher cipher = Cipher.getInstance("RSA/None/NoPadding\", \"BC\"");
        cipher.init(Cipher.ENCRYPT_MODE, globals.pubKey);
        CipherOutputStream cipherOut = new CipherOutputStream(out, cipher);
        byte[] fileBuffer = new byte[8192];
        InputStream fileReader = new BufferedInputStream(fis);
        int bytesRead;
        while((bytesRead = fileReader.read(fileBuffer)) > 0){
            cipherOut.write(fileBuffer, 0, bytesRead);
        }
        cipherOut.flush();
        cipherOut.close();

        Log.Write("Finished sending");
        socket = ConnectionListener.ListenForIncomingConnections(localTreeModel,servSock);
        return 0;
    }

}