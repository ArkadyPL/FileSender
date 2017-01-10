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
    static Object current_file = null;
    static ServerSocket senderSocket = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println(Inet4Address.getLocalHost().getHostAddress());
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
        //tree
        // Figure out where in the filesystem to start displaying
        File root;
        if (args.length > 0) root = new File(args[0]);
        else root = new File(System.getProperty("user.home"));

        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        // Create a JTree and tell it to display our model
        JTree localTree = new JTree();
        JTree remoteTree = new JTree();
        localTree.setModel(model);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        // The JTree can get big, so allow it to scroll.
        JScrollPane scrollpane = new JScrollPane(localTree);
        JScrollPane scrollpane2 = new JScrollPane(remoteTree);
        // Display it all in a window and make the window appear
        // frame.add(scrollpane, "Center");
        frame.setSize( 1000, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        JLabel fileLabel = new JLabel("       Current file : " + current_file);
        JLabel IPlabel = new JLabel("      My local ip : " + Inet4Address.getLocalHost().getHostAddress());
        panel.add(scrollpane);
        panel.add(scrollpane2);
        panel.add(IPlabel);
        panel.add(fileLabel);
        frame.add(panel);
        frame.setVisible(true);
        //LISTENER GETS CURRENTLY POINTED DIRECTORY. sends on double click

        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = localTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = localTree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        System.out.println("Single"+selPath);
                        fileLabel.setText("       Current file : " + selPath.getLastPathComponent().toString());
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

        Receiver.work(remoteTree, frame, scrollpane2);
    }
}