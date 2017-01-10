package com.filesender;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.net.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Receiver {
    public static void work(JTree clientTree, JFrame frame, JScrollPane pane) throws FileNotFoundException, IOException, ClassNotFoundException {
        //byte[] buffer = new byte[maxsize];
        InetAddress adr = InetAddress.getByName(Inet4Address.getLocalHost().getHostAddress());
        Socket socket = null;
        while(true) {
            try{
                socket = new Socket(adr, 9990);
            }
            catch (Exception e){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                continue;
            }
            break;
        }
        InputStream in = socket.getInputStream();
        // OutputStream out = new FileOutputStream((String) current_file);
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
        Object serverTree;

        serverTree = inFromServer.readObject();
        DefaultMutableTreeNode new1 = new DefaultMutableTreeNode(serverTree);
        System.out.println("ROOOOT " +new1);
        while(serverTree != null) {
            try {
                serverTree= inFromServer.readObject();
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
        clientTree.setModel(new DefaultTreeModel(new1));
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