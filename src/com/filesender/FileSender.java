package com.filesender;

import com.filesender.HelperClasses.IPAddressValidator;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.tree.TreePath;

public class FileSender {
    static String localIP = null;
    static String remoteIP = null;
    static boolean isConnected = false;
    static volatile boolean isListening = true;
    static Socket connectionSocket = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        localIP = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("Your IP address is: " + localIP);

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
        File root = new File(System.getProperty("user.home")/*.substring(0, 3)*/);

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
            remoteIP = remoteIPTextField.getText();
            Boolean isValid = new IPAddressValidator().validate(remoteIP);
            System.out.println("Connection button clicked. Remote IP value: " + remoteIP  + "\tGiven IP address is " + (isValid ? "valid" : "not valid"));
            if(isValid) {
                isListening = false;
                try {
                    System.out.println("Trying to connect to remote server...");
                    connectionSocket = new Socket(remoteIP, 9990);
                } catch (ConnectException e) {
                    System.out.println("Remote is not available...");
                } catch (Exception e) {
                    System.out.println("Connection error...");
                }
                if (connectionSocket != null) {
                    System.out.println("Connected to remote!");
                    try {
                        Connectioner.ConnectToServer(connectionSocket);
                    } catch (IOException e) {
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
        //panel.add(remoteTreePane);
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
                        System.out.print("Double click on row #" + selRow + "\t File: " + selPath.getLastPathComponent());
                        if(isConnected){
                            //todo: send clicked file
                        }
                        else {
                            System.out.println(" !Not connected yet!");
                        }
                    }
                }
            }
        };
        localTree.addMouseListener(mouseListener);

        connectionSocket = ConnectionListener.ListenForIncomingConnections();
        //todo: receive remote commands and process them

        //Receiver.work(remoteTree, frame, remoteTreePane);
    }
}