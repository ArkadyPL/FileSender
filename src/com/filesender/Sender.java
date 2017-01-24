package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.Cryptography.AES;
import com.filesender.HelperClasses.ToSend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import javax.swing.tree.TreeModel;

/**
 * Class containing static methods supporting sending for Files or remote files' trees to the client.
 */
public class Sender {
    static Queue queue = new LinkedList();

    public static void sendTree(Socket connectedSocket, TreeModel localTreeModel, Operation rooot) throws IOException, ClassNotFoundException {
        ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
        File rootObj =  null;
        if(Objects.equals(rooot.argument1, "root")) {
            rootObj = new File(localTreeModel.getRoot().toString());
        }
        else {
            rootObj = new File(rooot.obj1.toString());
        }
        ToSend ts = new ToSend(rootObj,true);
        ostream.writeObject(AES.encrypt(ts));
        Log.Write("Sending the file tree...");
        for(int i = 0; i < localTreeModel.getChildCount(rootObj);i++) {
            if( localTreeModel.isLeaf(localTreeModel.getChild(rootObj,i)) ) {
                ToSend ts2 = new ToSend(localTreeModel.getChild(rootObj,i),false);
                //Log.WriteTerminal("sending: " + ts2.node);
                ostream.writeObject(AES.encrypt(ts2));
                queue.add(localTreeModel.getChild(rootObj,i));
            }
            else {
                ToSend ts2 = new ToSend(localTreeModel.getChild(rootObj,i),true);
                Log.WriteTerminal("sending: " + ts2.node);
                ostream.writeObject(AES.encrypt(ts2));
                queue.add(localTreeModel.getChild(rootObj,i));
            }
        }
        ostream.close();
        Log.WriteTerminal("SENDING DONE");
    }

    public static int sendFile(String current_file, Socket socket) throws IOException, ClassNotFoundException {
        Log.Write("Sending the file: " + current_file + "...");
        File myFile = new File(current_file);
        byte[] buffer = new byte[(int) myFile.length()];
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream in = new BufferedInputStream(fis);
        in.read(buffer,0,buffer.length);
        String length = ((Integer)buffer.length).toString();
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        Operation basicOperation = new Operation(10, length,null, buffer);
        ostream.writeObject(basicOperation.encryptFields());

        in.close();
        Log.Write("Finished sending");
        return 0;
    }

}