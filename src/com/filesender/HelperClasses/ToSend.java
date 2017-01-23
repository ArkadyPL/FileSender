package com.filesender.HelperClasses;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.net.Socket;

/**
 * This class is internally used by functions sending and receiving File Trees
 *
 * @see com.filesender.Receiver#receiveTree(JTree, Socket, Object, Boolean)
 * @see com.filesender.Sender#sendTree(Socket, TreeModel, Operation)
 */
public class ToSend implements java.io.Serializable {
    public Object node;
    public Boolean isRoot;
    public ToSend(Object _node, Boolean is) {
        node = _node;
        isRoot = is;
    }
}
