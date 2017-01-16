package com.filesender;

import com.filesender.GuiElements.ConnectionPanel;
import com.filesender.GuiElements.Toolbar;
import com.filesender.HelperClasses.IPAddressValidator;
import com.filesender.HelperClasses.globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.Objects;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;




public class FileSender {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        globals.localIP = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("Your IP address is: " + globals.localIP);
        globals.serverSocket = new ServerSocket(9990);
        //SETTING UP GUI WITH FILE PATHS TREE

        globals.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolbar toolbar = new Toolbar(globals.localIP);

        Container contentPane = globals.frame.getContentPane();
        contentPane.add(toolbar, BorderLayout.NORTH);

        // Figure out where in the filesystem to start displaying
        File root = new File(System.getProperty("user.home").toString().substring(0, 3));
        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        // Create a JTree and tell it to display our model

        globals.localTree.setModel(model);

        //GUI CREATION
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        JScrollPane localTreePane = new JScrollPane(globals.localTree);
        JScrollPane remoteTreePane = new JScrollPane(globals.remoteTree);//Not displayed when not connected

        // Display it all in a window and make the window appear
        globals.frame.setSize( 1000, 600); // Set frame size
        globals.frame.setLocationRelativeTo(null); // Put frame in center of the screen
        panel.add(localTreePane);
        panel.add(remoteTreePane);
        globals.frame.add(panel);
        globals.frame.setVisible(true);

        //LISTENER GETS CURRENTLY POINTED DIRECTORY. sends on double click
        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = globals.localTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = globals.localTree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        System.out.println("Single clicked : " + selPath.getLastPathComponent());
                    }
                    else if(e.getClickCount() == 2) {
                        System.out.println("Double click on row #" + selRow + "\t File: " + selPath.getLastPathComponent());
                        //todo: send file
                    }
                }
            }
        };
        globals.localTree.addMouseListener(mouseListener);

        MouseListener mouseListenerRemote = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = globals.remoteTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = globals.remoteTree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        System.out.println("Single clicked : " + selPath.getLastPathComponent());
                    }
                    else if(e.getClickCount() == 2) {
                        if (globals.remoteTree.getModel().isLeaf(new DefaultMutableTreeNode(selPath.toString())) && !Objects.equals(selPath.getLastPathComponent().toString(),"...")) {
                            System.out.println("Double click on row #" + selRow + "\t File: " + selPath.getLastPathComponent());
                            try {
                                globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                                Receiver.receiveFile(globals.connectionSocket, selPath.getLastPathComponent());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
        globals.remoteTree.addMouseListener(mouseListenerRemote);

        //When user acts with remoteFileTree
        TreeExpansionListener treeExpandListener = new TreeExpansionListener() {

            //If user expanded the tree
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                try {
                    if (Objects.equals(path.getParentPath().toString(), "[...]") != true ){
                        globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                        System.out.println("PREVIOUS ROOT before: "+ globals.previousDir);
                        Receiver.buildRemoteTree(globals.remoteTree, globals.connectionSocket, path.getLastPathComponent(),false);
                        globals.frame.repaint();
                        globals.frame.revalidate();
                        System.out.println("PREVIOUS ROOT after: "+ globals.previousDir);
                    }
                }
                catch(java.lang.NullPointerException e) {

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //If user collapsed the tree
            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                System.out.println("Collapsed" +globals.previousDir + " patho: " + path);
                try {
                    if (Objects.equals(path.toString(), "[...]") == true ){
                        if(globals.dirStack.isEmpty()) {
                            globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                            Receiver.buildRemoteTree(globals.remoteTree, globals.connectionSocket,"root",false);
                        }
                        else {
                            System.out.println("PREVIOUS ROOT before: " + globals.previousDir);
                            if (globals.dirStack.isEmpty() != true) {
                                globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                                Receiver.buildRemoteTree(globals.remoteTree, globals.connectionSocket, globals.previousDir, true);
                            }
                            System.out.println("PREVIOUS ROOT after: " + globals.previousDir);
                        }
                        globals.frame.repaint();
                        globals.frame.revalidate();
                    }
                }
                catch(java.lang.NullPointerException e) {

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        globals.remoteTree.addTreeExpansionListener(treeExpandListener);
        globals.connectionSocket = ConnectionListener.ListenForIncomingConnections(globals.localTree.getModel(),globals.serverSocket);
    }
}