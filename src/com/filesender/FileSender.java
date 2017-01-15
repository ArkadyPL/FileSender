package com.filesender;

import com.filesender.HelperClasses.IPAddressValidator;
import com.sun.org.apache.xpath.internal.operations.Bool;

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
    static String localIP = null;
    static InetAddress remoteIP = null;
    static volatile boolean isConnected = false;
    static Socket connectionSocket = null;
    static ServerSocket serverSocket = null;
    static Object previousRoot = null;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        localIP = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("Your IP address is: " + localIP);
        serverSocket = new ServerSocket(9990);
        //SETTING UP GUI WITH FILE PATHS TREE
        JFrame frame = new JFrame("File Sender");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel toolbarText1 = new JLabel("Your IP address is: ");

        JTextField ipLabel = new JTextField(localIP);
        ipLabel.setEditable(false);
        ipLabel.setBorder(null);
        ipLabel.setForeground(UIManager.getColor("Label.foreground"));
        ipLabel.setFont(UIManager.getFont("Label.font"));

        //ADDING TOOLBAR
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(toolbarText1);
        toolbar.add(ipLabel);


        Container contentPane = frame.getContentPane();
        contentPane.add(toolbar, BorderLayout.NORTH);

        // Figure out where in the filesystem to start displaying
        File root = new File(System.getProperty("user.home"));
        //File root = new File("C:\\");
        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        // Create a JTree and tell it to display our model
        JTree localTree = new JTree();
        JTree remoteTree = new JTree();
        localTree.setModel(model);


        //GUI CREATION
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        JScrollPane localTreePane = new JScrollPane(localTree);
        JScrollPane remoteTreePane = new JScrollPane(remoteTree);//Not displayed when not connected
        //Panel for starting connection
        JPanel controlPanel = new JPanel();
        JLabel promptText = new JLabel("Remote IP:");
        controlPanel.add(promptText);
        JTextField remoteIPTextField = new JTextField(30);
        controlPanel.add(remoteIPTextField);
        JButton connectButon = new JButton("Connect");

        connectButon.setAlignmentY(Component.CENTER_ALIGNMENT);
        connectButon.addActionListener(ae -> {
            try {
                remoteIP = InetAddress.getByName(remoteIPTextField.getText());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            Boolean isValid = new IPAddressValidator().validate(remoteIPTextField.getText());
            System.out.println("Connection button clicked. Remote IP value: " + remoteIP  + "\tGiven IP address is " + (isValid ? "valid" : "not valid"));
            if(isValid) {
                try {
                    System.out.println("Trying to connect to remote server...");
                    connectionSocket = new Socket(remoteIP, 9990);
                } catch (ConnectException e) {
                    System.out.println("Remote is not available...");
                } catch (Exception e) {
                    System.out.println("Connection error...");
                }
                if (connectionSocket != null) {
                    isConnected = true;
                    System.out.println("Connected to remote!");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Receiver.work(remoteTree,frame,connectionSocket,"root");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        controlPanel.add(connectButon);

        // Display it all in a window and make the window appear
        frame.setSize( 1000, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        panel.add(localTreePane);
        panel.add(remoteTreePane);
        panel.add(controlPanel);
        frame.add(panel);
        frame.setVisible(true);

        //LISTENER GETS CURRENTLY POINTED DIRECTORY. sends on double click
        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = localTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = localTree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        System.out.println("Single clicked : " + selPath.getLastPathComponent());
                    }
                    else if(e.getClickCount() == 2) {
                        System.out.println("Double click on row #" + selRow + "\t File: " + selPath.getLastPathComponent());
                        if(isConnected){
                            //todo: send clicked file
                            System.out.println("Sending choosen file...");
                        }
                        else {
                            System.out.println("!Not connected yet!");
                        }
                    }
                }
            }
        };
        localTree.addMouseListener(mouseListener);

        MouseListener mouseListenerRemote = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = remoteTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = remoteTree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        System.out.println("Single clicked : " + selPath.getLastPathComponent());
                    }
                    else if(e.getClickCount() == 2) {
                        System.out.println("Double click on row #" + selRow + "\t File: " + selPath.getLastPathComponent());

                        if(isConnected){
                            //todo: send clicked file
                            System.out.println("Sending choosen file...");
                        }
                        else {
                            System.out.println("!Not connected yet!");
                        }
                    }
                }
            }
        };
        remoteTree.addMouseListener(mouseListenerRemote);


        TreeExpansionListener treeExpandListener = new TreeExpansionListener() {

            public void treeExpanded(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                try {
                    if (Objects.equals(path.getParentPath().toString(), "[...]") != true ){
                        connectionSocket = new Socket(remoteIP, 9990);
                        previousRoot = remoteTree.getModel().getChild(remoteTree.getModel().getRoot(),0);
                        Receiver.work(remoteTree, frame, connectionSocket, path.getLastPathComponent());
                    }
                    else {
                        if(previousRoot != null && previousRoot != System.getProperty("user.home") ) {
                            Receiver.work(remoteTree, frame, connectionSocket, previousRoot);
                        }
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

                //  String data = node.getUserObject().toString();

            }

            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                String data = node.getUserObject().toString();
                System.out.println("Collapsed: " + data);
            }
        };
        remoteTree.addTreeExpansionListener(treeExpandListener);

        connectionSocket = ConnectionListener.ListenForIncomingConnections(localTree.getModel(),serverSocket);
        //todo: receive remote commands and process them

        //Receiver.work(remoteTree, frame, remoteTreePane);
    }
}