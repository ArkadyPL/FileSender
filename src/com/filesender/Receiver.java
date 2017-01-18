package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;

import java.io.*;
import java.net.Socket;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Receiver {
    public static void buildRemoteTree(JTree remoteTree, Socket socket,Object dir, Boolean back) throws FileNotFoundException, IOException, ClassNotFoundException {
        if(globals.isConnected) {
            if (globals.previousDir != null) {
                if (back != true) {
                    globals.previousDir = remoteTree.getModel().getChild(remoteTree.getModel().getRoot(), 0);
                    globals.dirStack.add(globals.previousDir);
                } else {
                    if (!globals.dirStack.isEmpty())
                        dir = globals.dirStack.pop();
                }
            }
            ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
            operation basicOperation = new operation(1, dir.toString(), null, dir);
            ostream.writeObject(basicOperation);
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
            to_send serverTreeNode;
            serverTreeNode = (to_send) inFromServer.readObject();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("...");
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(serverTreeNode.node);
            if (inFromServer == null) {
                node1.add(new DefaultMutableTreeNode());
            } else {
                while (serverTreeNode != null) {
                    try {
                        serverTreeNode = (to_send) inFromServer.readObject();
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
            if (globals.previousDir == null) {
                globals.previousDir = remoteTree.getModel().getChild(remoteTree.getModel().getRoot(), 0);
                globals.dirStack.add(globals.previousDir);
            }
        }
    }

    public static void receiveFile(Socket socket, Object filePath) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        if (globals.isConnected) {
            String fileName = filePath.toString();
            ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
            operation basicOperation = new operation(2, fileName, null, filePath);
            ostream.writeObject(basicOperation);
            Path p = Paths.get(fileName);
            String fileSaveName = p.getFileName().toString();

            InputStream is = socket.getInputStream();
            File test = new File(System.getProperty("user.home") + "\\Desktop\\" + fileSaveName);
            test.createNewFile();
          //  FileOutputStream fos = new FileOutputStream(test);
          //  BufferedOutputStream out = new BufferedOutputStream(fos);

//            int count;
//            byte[] buffer = new byte[8192]; // or 4096, or more
//            while ((count = is.read(buffer)) > 0) {
//                out.write(buffer, 0, count);
//            }

            Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, globals.pubKey);
            CipherInputStream cipherIn = new CipherInputStream(is, cipher);

            byte[] fileBuffer = new byte[8192];
            FileOutputStream fileWriter = new FileOutputStream(test);
            int bytesRead;
            while((bytesRead = cipherIn.read(fileBuffer)) > 0){
                fileWriter.write(fileBuffer, 0, bytesRead);
            }
            fileWriter.flush();
            fileWriter.close();
            is.close();
            Log.Write("File: " + fileSaveName + " downloaded.");
        }
    }
}