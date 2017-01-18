package com.filesender;

import com.filesender.Cryptography.AES;
import com.filesender.HelperClasses.*;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Receiver {

    public static void receiveTree(JTree remoteTree, Socket socket,Object dir, Boolean back) throws FileNotFoundException, IOException, ClassNotFoundException {
        if( !globals.isConnected ) return;
        if(globals.previousDir != null) {
            if(back != true) {
                globals.previousDir = remoteTree.getModel().getChild(remoteTree.getModel().getRoot(), 0);
                globals.dirStack.add(globals.previousDir);
            }else {
                if (!globals.dirStack.isEmpty())
                    dir = globals.dirStack.pop();
            }
        }

        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        Operation basicOperation = new Operation(1,dir.toString(),null,dir);
        ostream.writeObject(basicOperation.encryptFields());
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
        ToSend serverTreeNode;
        serverTreeNode = (ToSend)AES.decrypt((byte[])inFromServer.readObject());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("...");
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(serverTreeNode.node);
        if(inFromServer == null) {
            node1.add(new DefaultMutableTreeNode());
        }
        else {
            while (serverTreeNode != null) {
                try {
                    serverTreeNode = (ToSend) AES.decrypt((byte[])inFromServer.readObject());
                } catch (java.io.EOFException e) {
                    break;
                }
                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(serverTreeNode.node);
                if (serverTreeNode.isRoot == true) {
                    node2.add(new DefaultMutableTreeNode());
                }
                node1.add(node2);
            }
        }
        Log.WriteTerminal("Tree received");
        root.add(node1);
        remoteTree.setModel(new DefaultTreeModel(root));
        TreePath j = new TreePath(node1.getPath());
        remoteTree.expandPath(j);
        if(globals.previousDir == null) {
            globals.previousDir = remoteTree.getModel().getChild(remoteTree.getModel().getRoot(), 0);
            globals.dirStack.add(globals.previousDir);
        }
    }

    public static void receiveFile(Socket socket, Object filePath) throws IOException {
        if( !globals.isConnected ) return;
        String fileName = filePath.toString();
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        Operation basicOperation = new Operation(2,fileName,null,filePath);
        ostream.writeObject(basicOperation.encryptFields());
        Path p = Paths.get(fileName);
        String fileSaveName = p.getFileName().toString();

        Log.Write("Receiving the file \"" + fileSaveName + "\"");
        InputStream is = socket.getInputStream();
        File encryptedFile = new File(System.getProperty("user.home") + "\\Desktop\\"+fileSaveName);
        encryptedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(encryptedFile);
        BufferedOutputStream out = new BufferedOutputStream(fos);
        int count;
        byte[] buffer = new byte[8192]; // or 4096, or more
        while ((count = is.read(buffer)) > 0)
        {
            out.write(buffer, 0, count);
        }
        Log.Write("File \"" + fileSaveName + "\" saved");
    }
}