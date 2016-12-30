package com.filesender;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import javax.swing.tree.TreePath;

public class FileSender {
    static Object current_file = null;
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(Inet4Address.getLocalHost().getHostAddress());
        //GETTING IP
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ip = null; //you get the IP as a String
        try {
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ip);
        //END OF GETTING IP


        //SETTING UP GUI WITH FILE PATHS TREE
        JFrame frame = new JFrame("File Sender");
        //tree
        // Figure out where in the filesystem to start displaying
        File root;
        if (args.length > 0) root = new File(args[0]);
        else root = new File(System.getProperty("user.home"));

        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        // Create a JTree and tell it to display our model
        JTree tree = new JTree();
        tree.setModel(model);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        // The JTree can get big, so allow it to scroll.
        JScrollPane scrollpane = new JScrollPane(tree);
        JScrollPane scrollpane2 = new JScrollPane();
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
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        System.out.println("Single"+selPath);
                        fileLabel.setText("       Current file : " + selPath.getLastPathComponent().toString());
                    }
                    else if(e.getClickCount() == 2) {

                        System.out.println("Double"+selRow);
                        try {
                            Server.work(selPath.getLastPathComponent().toString(), tree);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);
    }
}