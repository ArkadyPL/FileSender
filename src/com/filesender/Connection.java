package com.filesender;

import com.filesender.HelperClasses.Log;

import com.filesender.HelperClasses.ServerStatus;
import com.filesender.HelperClasses.globals;

import javax.swing.tree.TreeModel;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.interfaces.RSAPublicKey;

public class Connection {

    public static void sendKey(Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        operation basicOperation = new operation(5, null,null, globals.pubKey);
        ostream.writeObject(basicOperation);
    }
    public static void sendBackKey(operation basicOp, TreeModel localTreeModel, ServerSocket serverSocket, Socket connectedSocket) throws IOException, ClassNotFoundException {
        globals.remoteKey = (RSAPublicKey)basicOp.obj1;
        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));

        ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
        operation basicOperation = new operation(6, null,null, globals.pubKey);
        ostream.writeObject(basicOperation);
        connectedSocket = ConnectionListener.ListenForIncomingConnections(localTreeModel, serverSocket);
    }
    public static void getKey(operation basicOp, TreeModel localTreeModel, ServerSocket serverSocket, Socket connectedSocket) throws IOException, ClassNotFoundException {
        globals.remoteKey = (RSAPublicKey)basicOp.obj1;
        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));
        connectedSocket = ConnectionListener.ListenForIncomingConnections(localTreeModel, serverSocket);
    }
}
