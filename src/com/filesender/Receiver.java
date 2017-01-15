package com.filesender;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.net.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Receiver {
    public static void work(JTree clientTree, JFrame frame, Socket socket,Object dir) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        operation basicOperation = new operation(1,dir.toString(),null,dir);
        ostream.writeObject(basicOperation);
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
        to_send serverTreeNode;
        serverTreeNode = (to_send)inFromServer.readObject();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("...");
        DefaultMutableTreeNode new1 = new DefaultMutableTreeNode(serverTreeNode.node);
        System.out.println("ROOOOT " +new1);
        while(serverTreeNode != null) {
            try {
                serverTreeNode = (to_send)inFromServer.readObject();
            }
            catch (java.io.EOFException e) {
                break;
            }
            DefaultMutableTreeNode new2 = new DefaultMutableTreeNode(serverTreeNode.node);
            if(serverTreeNode.isRoot == true) {
                new2.add(new DefaultMutableTreeNode());
            }
            new1.add(new2);
            System.out.println("Latter: " + new2);
        }
        System.out.println("Tree received");
        root.add(new1);
        clientTree.setModel(new DefaultTreeModel(root));
        frame.repaint();
        frame.revalidate();
        TreePath j = new TreePath(new1.getPath());
        clientTree.expandPath(j);
        //  System.out.println("Children "+serverTree.getChildCount(serverTree.getRoot()));


        /*
        int count;
        byte[] buffer = new byte[8192];
        while((count =in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }*/
        System.out.print("Server file tree received");
        //out.flush();

    }
}