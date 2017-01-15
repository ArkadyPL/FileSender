package com.filesender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import javax.swing.tree.TreeModel;
class to_send implements java.io.Serializable {
    Object node;
    Boolean isRoot;
    public to_send(Object _node, Boolean is) {
        node = _node;
        isRoot = is;
    }
}

public class Sender {
    static OutputStream out = null;
    static Socket socket = null;
    static Queue queue = new LinkedList();
    public static void sendTree(Socket connectedSocket, TreeModel localTreeModel, ServerSocket servSock, operation rooot) throws IOException, ClassNotFoundException {
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
        for(int i = 0; i < localTreeModel.getChildCount(rootObj);i++) {
            if(localTreeModel.isLeaf(localTreeModel.getChild(rootObj,i)) == true) {
                to_send ts2 = new to_send(localTreeModel.getChild(rootObj,i),false);
                System.out.println("sending: " + ts2.node);
                ostream.writeObject(ts2);
                queue.add(localTreeModel.getChild(rootObj,i));
            }
            else {
                to_send ts2 = new to_send(localTreeModel.getChild(rootObj,i),true);
                System.out.println("sending: " + ts2.node);
                ostream.writeObject(ts2);
                queue.add(localTreeModel.getChild(rootObj,i));
            }
        }

        ostream.close();
        System.out.println("SENDING DONE");
        connectedSocket = ConnectionListener.ListenForIncomingConnections(localTreeModel,servSock);
    }
   /* public static int work(String current_file,  TreeModel localTreeModel, ServerSocket sock) throws IOException, ClassNotFoundException {
        System.out.println("server to send: " + current_file);

        // File myFile = new File(current_file);
        // byte[] buffer = new byte[(int) myFile.length()];

        //accept starts new connection between local and current socket
        //todo: before .accept; ping to check if remote app is available
        socket = sock.accept();
        System.out.println("parento " + localTreeModel.getRoot());
        System.out.println("childo " +localTreeModel.getChildCount(localTreeModel.getRoot()));
        System.out.println("Accepted connection from : " + socket);
        ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
        Sender.sendTree(socket, localTreeModel,);

        /* CURRENTLY OFF to deal with tree
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream in = new BufferedInputStream(fis);
        in.read(buffer,0,buffer.length);
        out = socket.getOutputStream();
        System.out.println("Sending files");
        out.write(buffer,0, buffer.length);
        out.flush();
        /*while ((count = in.read(buffer)) > 0){
            out.write(buffer,0,count);
            out.flush();
        }
        //out.close();
        //  in.close();
        socket.close();
        System.out.println("Finished sending");
        return 3;
    }*/

}