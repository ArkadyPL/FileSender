package com.filesender;

import com.filesender.HelperClasses.globals;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class Receiver {

    static int maxsize = 999999999;
    static int byteread;
    static int current = 0;
    public static void work(JTree clientTree, JFrame frame, Socket socket,Object dir, Boolean back) throws FileNotFoundException, IOException, ClassNotFoundException {
        if(globals.previousDir != null) {
            if(back != true) {
                globals.previousDir = clientTree.getModel().getChild(clientTree.getModel().getRoot(), 0);
                globals.dirStack.add(globals.previousDir);
            }else {
                if (!globals.dirStack.isEmpty())
                    dir = globals.dirStack.pop();
            }
        }
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        operation basicOperation = new operation(1,dir.toString(),null,dir);
        ostream.writeObject(basicOperation);
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
        to_send serverTreeNode;
        serverTreeNode = (to_send)inFromServer.readObject();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("...");
        DefaultMutableTreeNode new1 = new DefaultMutableTreeNode(serverTreeNode.node);
        if(inFromServer == null) {
            new1.add(new DefaultMutableTreeNode());
        }
        else {
            while (serverTreeNode != null) {
                try {
                    serverTreeNode = (to_send) inFromServer.readObject();
                } catch (java.io.EOFException e) {
                    break;
                }
                DefaultMutableTreeNode new2 = new DefaultMutableTreeNode(serverTreeNode.node);
                if (serverTreeNode.isRoot == true) {
                    new2.add(new DefaultMutableTreeNode());
                }
                new1.add(new2);
            }
        }
        System.out.println("Tree received");
        root.add(new1);
        clientTree.setModel(new DefaultTreeModel(root));
        frame.repaint();
        frame.revalidate();
        TreePath j = new TreePath(new1.getPath());
        clientTree.expandPath(j);
        if(globals.previousDir == null) {
            globals.previousDir = clientTree.getModel().getChild(clientTree.getModel().getRoot(), 0);
            globals.dirStack.add(globals.previousDir);
        }
    }
    public static void receiveFile(Socket socket,String fileName, Object filePath) throws IOException {

        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        operation basicOperation = new operation(2,fileName,null,filePath);
        ostream.writeObject(basicOperation);
        Path p = Paths.get(fileName);
        String fileSaveName = p.getFileName().toString();
        byte[] buffer = new byte[maxsize];
        InputStream is = socket.getInputStream();
        File test = new File(System.getProperty("user.home") + "\\Desktop\\"+fileSaveName);
        test.createNewFile();
        FileOutputStream fos = new FileOutputStream(test);
        BufferedOutputStream out = new BufferedOutputStream(fos);
        byteread = is.read(buffer, 0, buffer.length);
        current = byteread;

        while (byteread > -1){
            System.out.println("hello7777oo");
            byteread = is.read(buffer, 0, buffer.length - current);
            if (byteread >= 0) current += byteread;
        }
        out.write(buffer, 0, current);
        out.flush();
        fos.close();
        is.close();
        System.out.println("byeeee");
    }
}