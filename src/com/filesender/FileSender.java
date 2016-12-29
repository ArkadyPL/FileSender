package com.filesender;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class FileSender {
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
        Object ptr = null;
        // Create a JTree and tell it to display our model
        JTree tree = new JTree();
        tree.setModel(model);
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(1,2));
        // The JTree can get big, so allow it to scroll.
        JScrollPane scrollpane = new JScrollPane(tree);

        // Display it all in a window and make the window appear
        //frame.add(scrollpane, "Center");
        frame.setSize( 1000, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        JLabel IPlabel = new JLabel("       Moje  ip : " + ip);
        panel.add(scrollpane);
        panel.add(IPlabel);
        frame.add(panel);
        frame.setVisible(true);
        //LISTENER GETS CURRENTLY POINTED DIRECTORY
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                Object object = e.getPath().getLastPathComponent();
                if (object instanceof File){
                    File file = (File) object;
                }
                System.out.println("You selected " + object);
            }
        });
        //STARTING AS A SERVER - CLIENT ON OTHER PC
        try {
            Server.work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}