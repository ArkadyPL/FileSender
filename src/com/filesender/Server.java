package com.filesender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Piotr on 29.12.2016.
 */
public class Server {
    static ServerSocket receiver = null;
    static OutputStream out = null;
    static Socket socket = null;
    static File myFile = new File("C:\\Users\\Piotr\\Desktop\\test\\tekst.txt");
    /*static int count;*/
    static byte[] buffer = new byte[(int) myFile.length()];
    public static int work() throws IOException {
        receiver = new ServerSocket(9999);
        socket = receiver.accept();
        System.out.println("Accepted connection from : " + socket);
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
        in.close();
        socket.close();
        System.out.println("Finished sending");
        return 3;
    }
}
