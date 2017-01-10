package com.filesender;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import javax.swing.tree.TreePath;

public class FileSender {
    static ServerSocket senderSocket = null;
    static String LocalIP = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LocalIP = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("Your IP address is: " + LocalIP);
        //GETTING IP
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
      /* try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*
        String ip = null; //you get the IP as a String
        try {
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ip);
        //END OF GETTING IP
        */

        //SETTING UP GUI WITH FILE PATHS TREE
        JFrame frame = new JFrame("File Sender");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel toolbarText1 = new JLabel("Your IP address is: ");

        JTextField ipLabel = new JTextField(LocalIP);
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
        File root = new File(System.getProperty("user.home").substring(0, 3));

        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        // Create a JTree and tell it to display our model
        JTree localTree = new JTree();
        JTree remoteTree = new JTree();
        localTree.setModel(model);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        // The JTree can get big, so allow it to scroll.
        JScrollPane localTreePane = new JScrollPane(localTree);
        JScrollPane remoteTreePane = new JScrollPane(remoteTree);
        // Display it all in a window and make the window appear
        frame.setSize( 1000, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        panel.add(localTreePane);
        panel.add(remoteTreePane);
        frame.add(panel);
        frame.setVisible(true);
        //LISTENER GETS CURRENTLY POINTED DIRECTORY. sends on double click

        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = localTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = localTree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        System.out.println("Current : " + selPath.getLastPathComponent());
                    }
                    else if(e.getClickCount() == 2) {
                        ServerSocket sock = null;
                        try {
                            sock = new ServerSocket(9990);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        //todo: if sock port number == 9990, find another

                        System.out.println("Double"+selRow);
                        try {
                            Sender.work(selPath.getLastPathComponent().toString(), localTree.getModel(), sock);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        };
        localTree.addMouseListener(ml);

        Receiver.work(remoteTree, frame, remoteTreePane);
    }
}