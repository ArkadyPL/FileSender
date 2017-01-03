package com.filesender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.tree.TreeModel;


public class Server {
    static OutputStream out = null;
    static Socket socket = null;

    /*static int count;*/

    public static int work(String current_file,  TreeModel clientTreeModel, ServerSocket sock) throws IOException {
        System.out.println("server to send: " + current_file);

       // File myFile = new File(current_file);
       // byte[] buffer = new byte[(int) myFile.length()];

        socket = sock.accept();
        System.out.println("parento " + clientTreeModel.getRoot());
        System.out.println("childo "+clientTreeModel.getChildCount(clientTreeModel.getRoot()));
        Object new1 = clientTreeModel.getRoot();
        System.out.println("Accepted connection from : " + socket);
        ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
        outToClient.writeObject(new1);
        for(int i = 0; i < clientTreeModel.getChildCount(new1);i++) {
            outToClient.writeObject(clientTreeModel.getChild(new1,i));
        }
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
        //out.close();
      //  in.close();
        socket.close();
        System.out.println("Finished sending");
        return 3;
    }
}
