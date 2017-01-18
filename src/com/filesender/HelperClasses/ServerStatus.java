package com.filesender.HelperClasses;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Piotr on 17.01.2017.
 */
public class ServerStatus extends Thread{
    @Override
    public void run() {
        Socket s=null;
        while(true) {
            if (globals.remoteIP != null) {
                try {
                    s = new Socket(globals.remoteIP, 7899);
                    System.out.println("Server available");
                    s.close();
                } catch (IOException e) {

                    globals.isConnected = false;
                    Log.Write("Connection lost!");
                    globals.previousDir = null;
                    globals.remoteTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("<No connection>")));
                    globals.remoteTree.setEnabled(false);
                    globals.statusSocket = null;
                    break;
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
