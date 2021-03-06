package com.filesender;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;

/**
 * Class for logical maintenance of local and remote file trees that are displayed on the screen using JTree class.
 */
public class FileTreeModel implements TreeModel,java.io.Serializable {
    /**
     * Root for the model.
     */
    protected File root;

    /**
     * We specify the root directory when we create the model.
     * @param root Root to be saved as our.
     */
    public FileTreeModel(File root) { this.root = root; }

    /**
     * The model knows how to return the root object of the tree.
     * @return
     */
    public Object getRoot() { return root; }

    /**
     * Tell JTree whether an object in the tree is a leaf or not.
     * @param node
     * @return
     */
    public boolean isLeaf(Object node) {  return ((File)node).isFile(); }

    /**
     * Tell JTree how many children a node has.
     */
    public int getChildCount(Object parent) {
        String[] children = ((File)parent).list();
        if (children == null) return 0;
        return children.length;
    }

    /**
     * Fetch any numbered child of a node for the JTree.
     * Our model returns File objects for all nodes in the tree.
     * The JTree displays these by calling the File.toString() method.
     * @param parent
     * @param index
     * @return
     */
    public Object getChild(Object parent, int index) {
        String[] children = ((File)parent).list();
        if ((children == null) || (index >= children.length)) return null;
        return new File((File) parent, children[index]);
    }

    /**
     * Figure out a child's position in its parent node.
     */
    public int getIndexOfChild(Object parent, Object child) {
        String[] children = ((File)parent).list();
        if (children == null) return -1;
        String childname = ((File)child).getName();
        for(int i = 0; i < children.length; i++) {
            if (childname.equals(children[i])) return i;
        }
        return -1;
    }

    /**
     * This method is only invoked by the JTree for editable trees.
     * This TreeModel does not allow editing, so we do not implement this method.
     * The JTree editable property is false by default.
     * @param path
     * @param newvalue
     */
    public void valueForPathChanged(TreePath path, Object newvalue) {}

    /**
     * Since this is not an editable tree model, we never fire any events,
     * so we don't actually have to keep track of interested listeners.
     * @param l
     */
    public void addTreeModelListener(TreeModelListener l) {}

    /**
     * Since this is not an editable tree model, we never fire any events,
     * so we don't actually have to keep track of interested listeners.
     * @param l
     */
    public void removeTreeModelListener(TreeModelListener l) {}
}