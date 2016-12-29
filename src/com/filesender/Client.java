package com.filesender;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Piotr on 29.12.2016.
 */
public class Client {
    public static void work() throws FileNotFoundException, IOException {
        //byte[] buffer = new byte[maxsize];
        InetAddress adr = InetAddress.getByName("192.168.2.84");
        Socket socket = new Socket(adr,9999);
        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream("C:\\Users\\pmore\\Desktop\\test\\tekst.txt");
        int count;
        byte[] buffer = new byte[8192];
        while((count =in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }
        System.out.print("done");
        out.flush();
        socket.close();
    }
}
