package com.filesender.HelperClasses;

import com.filesender.AppState;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.IOException;
import java.net.Socket;

/**
 * Class for checking weather the server that we have connected to before is still available.
 * It is turned on in the beginning of {@link com.filesender.FileSender#main(String[])}.
 */
public class ServerStatus extends Thread{

    /**
     * Function that is running for the whole application's lifetime.
     * It is responsible for checking if remote that we are connected to is still available.
     */
    @Override
    public void run() {
        while(true) {
            Log.WriteTerminal("Checking remote availability...");
            //If we are connected to someone
            if (globals.remoteIP != null) {
                //Try to connect to remote socket to see if connection is still maintained
                try(Socket s = new Socket(globals.remoteIP, 7899)) {
                    Log.WriteTerminal("Server available");
                    s.close();
                } catch (IOException e) {
                    //If didn't manage to connect to remote, set program to 'unconnected state'
                    Log.Write("Connection lost!");
                    AppState.changeToDisconnected();
                    break;
                }
            }else{
                Log.Write("Not connected to any remote!");
            }

            try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}