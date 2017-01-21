package com.filesender.HelperClasses;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.IOException;
import java.net.Socket;

public class ServerStatus extends Thread{
    @Override
    public void run() {
        while(true) {
            if (globals.remoteIP != null) {
                try(Socket s = new Socket(globals.remoteIP, 7899)) {
                    Log.WriteTerminal("Server available");
                    s.close();
                } catch (IOException e) {
                    globals.isConnected = false;
                    Log.Write("Connection lost!");
                    globals.previousDir = null;
                    globals.remoteTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("<No connection>")));
                    break;
                }
            }

            try { Thread.sleep(5000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}