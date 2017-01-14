package com.filesender;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.net.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Receiver {
    public static void work(JTree clientTree, JFrame frame, Socket socket) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        operation basicOperation = new operation(1,null,null);
        ostream.writeObject(basicOperation);
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
        Object serverTree;
        serverTree = inFromServer.readObject();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("...");
        DefaultMutableTreeNode new1 = new DefaultMutableTreeNode(serverTree);
        System.out.println("ROOOOT " +new1);
        while(serverTree != null) {
            try {
                serverTree = inFromServer.readObject();
            }
            catch (java.io.EOFException e) {
                System.out.println("chill");
                break;
            }
            DefaultMutableTreeNode new2 = new DefaultMutableTreeNode(serverTree);
            new2.setAllowsChildren(true);
            new1.add(new2);

            System.out.println("Latter: " + new2);
        }
        System.out.println("G SHIT: ");
        root.add(new1);
        clientTree.setModel(new DefaultTreeModel(root));
        frame.repaint();
        frame.revalidate();
        //  System.out.println("Children "+serverTree.getChildCount(serverTree.getRoot()));


        /*
        int count;
        byte[] buffer = new byte[8192];
        while((count =in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }*/
        System.out.print("Server file tree received");
        //out.flush();
        socket.close();
    }
}