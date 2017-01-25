package com.filesender;

import com.filesender.HelperClasses.globals;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Class containing methods that allow to change app state to connected or disconnected.
 * It is done in both visual and logical way.
 */
public class AppState {
    /**
     * Method for changing the app state to disconnected.
     */
    public static void changeToDisconnected(){
        globals.remoteIP = null;
        globals.previousDir = null;
        globals.connectButton.setText("Connect");
        globals.remoteTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("<No connection>")));
        globals.remoteIPTextField.setEditable(true);
        globals.remotePinTextField.setEditable(true);
    }

    /**
     * Method for changing the app state to connected.
     */
    public static void changeToConnected(){
        globals.connectButton.setText("Disconnect");
        globals.remoteIPTextField.setEditable(false);
        globals.remotePinTextField.setEditable(false);
    }
}
