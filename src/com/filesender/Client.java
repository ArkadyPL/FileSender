package com.filesender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import javax.swing.tree.TreePath;
/**
 * Created by Piotr on 29.12.2016.
 */
public class Client {
    public static void work(Object current_file, JTree clientTree) throws FileNotFoundException, IOException, ClassNotFoundException {
        //byte[] buffer = new byte[maxsize];
        InetAddress adr = InetAddress.getByName("192.168.2.84");
        Socket socket = new Socket(adr,9999);
        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream((String) current_file);
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
        JTree serverTree=null;
        serverTree = (JTree)inFromServer.readObject();
        clientTree = serverTree;

        /*
        int count;
        byte[] buffer = new byte[8192];
        while((count =in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }*/
        System.out.print("done");
        out.flush();
        socket.close();
    }
}
