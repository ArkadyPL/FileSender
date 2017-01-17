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

    public static void sendKey(Socket socket,TreeModel localTreeModel, ServerSocket serverSocket) throws IOException, ClassNotFoundException, InterruptedException {
        Log.WriteTerminal("Sending PublicKey:\n" + DatatypeConverter.printHexBinary(globals.pubKey.getEncoded()));
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        operation basicOperation = new operation(5, globals.localIP,null, globals.pubKey);
        ostream.writeObject(basicOperation);

        socket = ConnectionListener.ListenForIncomingConnections(localTreeModel, serverSocket);
    }
    public static void sendBackKey(operation basicOp, TreeModel localTreeModel, ServerSocket serverSocket, Socket connectedSocket) throws IOException, ClassNotFoundException {
        globals.remoteKey = (RSAPublicKey)basicOp.obj1;
        Log.WriteTerminal("Sending back PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));
        connectedSocket = new Socket(globals.remoteIP,9990);
        ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
        operation basicOperation = new operation(6, null,null, globals.pubKey);
        ostream.writeObject(basicOperation);

    }
    public static void getKey(operation basicOp, TreeModel localTreeModel, ServerSocket serverSocket, Socket connectedSocket) throws IOException, ClassNotFoundException {
        globals.remoteKey = (RSAPublicKey)basicOp.obj1;
        Log.WriteTerminal("Getting PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));
        connectedSocket = new Socket(globals.remoteIP,9990);
        ObjectOutputStream ostream = new ObjectOutputStream(connectedSocket.getOutputStream());
        operation basicOperation = new operation(1, "root",null, "root");
        ostream.writeObject(basicOperation);
        Receiver.buildRemoteTree(globals.remoteTree,globals.connectionSocket,"root",false);
    }
}
