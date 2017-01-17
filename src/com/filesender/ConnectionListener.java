package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

//   ID LIST
/*  0 - keepalive - connection test
    1 - Rebuild tree for argument as a root
    2 - Send given file
    3 - Receive file of given name
    4 - Rename given file
 */
class operation implements java.io.Serializable {
    int opID;
    String argument1;
    String argument2;
    Object obj1;
    public operation(int _ID, String arg1,String arg2, Object _obj1) {
        opID = _ID;
        argument1 = arg1;
        argument2 = arg2;
        obj1 = _obj1;
    }
}

public class ConnectionListener {
    public static Socket ListenForIncomingConnections(TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException {

        Socket connectedSocket;
        Log.Write("Waiting for incoming connections...");

        connectedSocket = serverSocket.accept();

        Log.Write("Connection accepted!");
        if(globals.statusSocket == null)
            globals.statusSocket = new ServerSocket(7899);
        ObjectInputStream inFromServer = new ObjectInputStream(connectedSocket.getInputStream());
        operation basicOp;
        basicOp = (operation)inFromServer.readObject();
        if(basicOp.opID == 0) {

        }
        else if(basicOp.opID == 1) {
            Log.WriteTerminal("Operation ID 1 executed");
            Sender.sendTree(connectedSocket,localTreeModel,serverSocket,basicOp);
        }
        else if(basicOp.opID == 2) {
            Sender.sendFile(basicOp.argument1,localTreeModel,connectedSocket,serverSocket);
        }
        else if(basicOp.opID == 5){//save new public key from arg1 and send your public key in arg1
            Connection.sendBackKey(basicOp,localTreeModel,serverSocket,connectedSocket);
        }
        else if(basicOp.opID == 6) {
            Connection.getKey(basicOp,localTreeModel,serverSocket,connectedSocket);
        }
        return connectedSocket;
    }
}