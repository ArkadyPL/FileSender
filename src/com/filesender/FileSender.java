package com.filesender;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.net.*;
import java.io.*;
/**
 * Created by Arek on 20.12.2016.
 */
public class FileSender {
    public static void main(String[] args) {
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

        // The JTree can get big, so allow it to scroll.
        JScrollPane scrollpane = new JScrollPane(tree);

        // Display it all in a window and make the window appear
        //frame.add(scrollpane, "Center");
        frame.setSize( 1000, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        JLabel IPlabel = new JLabel("       Hello! Your IP is : " + ip);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        panel.add(scrollpane);
        panel.add(IPlabel);
        frame.add(panel);
        frame.setVisible(true);

    }
    //Komentarz testowy
}


class FileTreeModel implements TreeModel {
    // We specify the root directory when we create the model.
    protected File root;
    public FileTreeModel(File root) { this.root = root; }

    // The model knows how to return the root object of the tree
    public Object getRoot() { return root; }

    // Tell JTree whether an object in the tree is a leaf or not
    public boolean isLeaf(Object node) {  return ((File)node).isFile(); }

    // Tell JTree how many children a node has
    public int getChildCount(Object parent) {
        String[] children = ((File)parent).list();
        if (children == null) return 0;
        return children.length;
    }

    // Fetch any numbered child of a node for the JTree.
    // Our model returns File objects for all nodes in the tree.  The
    // JTree displays these by calling the File.toString() method.
    public Object getChild(Object parent, int index) {
        String[] children = ((File)parent).list();
        if ((children == null) || (index >= children.length)) return null;
        return new File((File) parent, children[index]);
    }

    // Figure out a child's position in its parent node.
    public int getIndexOfChild(Object parent, Object child) {
        String[] children = ((File)parent).list();
        if (children == null) return -1;
        String childname = ((File)child).getName();
        for(int i = 0; i < children.length; i++) {
            if (childname.equals(children[i])) return i;
        }
        return -1;
    }

    // This method is only invoked by the JTree for editable trees.
    // This TreeModel does not allow editing, so we do not implement
    // this method.  The JTree editable property is false by default.
    public void valueForPathChanged(TreePath path, Object newvalue) {}

    // Since this is not an editable tree model, we never fire any events,
    // so we don't actually have to keep track of interested listeners.
    public void addTreeModelListener(TreeModelListener l) {}
    public void removeTreeModelListener(TreeModelListener l) {}
}