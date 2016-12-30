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

public class Server {
    static ServerSocket receiver = null;
    static OutputStream out = null;
    static Socket socket = null;

    /*static int count;*/

    public static int work(String current_file, JTree tree) throws IOException {
        System.out.println("server to send: " + current_file);

        File myFile = new File(current_file);
        byte[] buffer = new byte[(int) myFile.length()];
        receiver = new ServerSocket(9999);
        socket = receiver.accept();
        System.out.println("Accepted connection from : " + socket);
        ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
        outToClient.writeObject(tree);

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
        }*/
        out.close();
      //  in.close();
        socket.close();
        System.out.println("Finished sending");
        return 3;
    }
}
