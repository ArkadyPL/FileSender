package com.filesender;

import com.filesender.GuiElements.Toolbar;
import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.bind.DatatypeConverter;


public class FileSender {

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        globals.statusSocket = new ServerSocket(7899);
        globals.remoteTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("<No connection>")));
        globals.remoteTree.setEnabled(false);
        globals.logTextArea.setAlignmentX(StyleConstants.ALIGN_CENTER);
        Cipher cipher = Cipher.getInstance("RSA");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);
        KeyPair kp = kpg.genKeyPair();
        globals.pubKey = (RSAPublicKey) kp.getPublic();
        globals.privKey = (RSAPrivateKey) kp.getPrivate();
        Log.WriteTerminal("Local PublicKey:\n" + DatatypeConverter.printHexBinary(globals.pubKey.getEncoded()));

        cipher.init(Cipher.ENCRYPT_MODE, globals.pubKey);

        globals.localIP = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("Your IP address is: " + globals.localIP);
        globals.serverSocket = new ServerSocket(9990);
        globals.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        globals.frame.setMinimumSize(new Dimension(720, 300));

        Toolbar toolbar = new Toolbar(globals.localIP);

        Container contentPane = globals.frame.getContentPane();
        contentPane.add(toolbar, BorderLayout.NORTH);

        // Figure out where in the filesystem to start displaying
        File root = new File(System.getProperty("user.home").substring(0, 3));
        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        // Create a JTree and tell it to display our model

        globals.localTree.setModel(model);

        //GUI CREATION
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel filesPanel = new JPanel();
        filesPanel.setLayout(new GridLayout(1,2));
        JScrollPane localTreePane = new JScrollPane(globals.localTree);
        JScrollPane remoteTreePane = new JScrollPane(globals.remoteTree);//Not displayed when not connected

        JScrollPane log = new JScrollPane (globals.logTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        log.setPreferredSize(new Dimension(1000, 140));

        // Display it all in a window and make the window appear
        globals.frame.setSize( 1000, 600); // Set frame size
        globals.frame.setLocationRelativeTo(null); // Put frame in center of the screen
        filesPanel.add(localTreePane);
        filesPanel.add(remoteTreePane);
        mainPanel.add(filesPanel, BorderLayout.CENTER);
        mainPanel.add(log, BorderLayout.PAGE_END);
        globals.frame.add(mainPanel);
        globals.frame.setVisible(true);

        //LISTENER GETS CURRENTLY POINTED DIRECTORY. sends on double click
        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = globals.localTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = globals.localTree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        Log.WriteTerminal("Single clicked : " + selPath.getLastPathComponent());
                    }
                    else if(e.getClickCount() == 2) {
                        Log.WriteTerminal("Double click on row #" + selRow + "\t File: " + selPath.getLastPathComponent());

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
                        Log.WriteTerminal("Single clicked : " + selPath.getLastPathComponent());
                    }
                    else if(e.getClickCount() == 2) {
                        if (globals.remoteTree.getModel().isLeaf(new DefaultMutableTreeNode(selPath.toString())) && !Objects.equals(selPath.getLastPathComponent().toString(),"...")) {
                            Log.WriteTerminal("Double click on row #" + selRow + "\t File: " + selPath.getLastPathComponent());
                            try {
                                globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                                Receiver.receiveFile(globals.connectionSocket, selPath.getLastPathComponent());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (NoSuchPaddingException e1) {
                                e1.printStackTrace();
                            } catch (NoSuchAlgorithmException e1) {
                                e1.printStackTrace();
                            } catch (InvalidKeyException e1) {
                                e1.printStackTrace();
                            } catch (NoSuchProviderException e1) {
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
                    if ( !Objects.equals(path.getParentPath().toString(), "[...]") ){
                        globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                        Log.WriteTerminal("PREVIOUS ROOT before: "+ globals.previousDir);
                        Receiver.buildRemoteTree(globals.remoteTree, globals.connectionSocket, path.getLastPathComponent(),false);
                        globals.frame.repaint();
                        globals.frame.revalidate();
                        Log.WriteTerminal("PREVIOUS ROOT after: "+ globals.previousDir);
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
                Log.WriteTerminal("Collapsed" +globals.previousDir + " patho: " + path);
                try {
                    if ( Objects.equals(path.toString(), "[...]") ){
                        if(globals.dirStack.isEmpty()) {
                            globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                            Receiver.buildRemoteTree(globals.remoteTree, globals.connectionSocket,"root",false);
                        }
                        else {
                            Log.WriteTerminal("PREVIOUS ROOT before: " + globals.previousDir);
                            if ( !globals.dirStack.isEmpty() ) {
                                globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                                Receiver.buildRemoteTree(globals.remoteTree, globals.connectionSocket, globals.previousDir, true);
                            }
                            Log.WriteTerminal("PREVIOUS ROOT after: " + globals.previousDir);
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
        Socket init;
        init = ConnectionListener.ListenForIncomingConnections(globals.localTree.getModel(),globals.serverSocket);
    }
}