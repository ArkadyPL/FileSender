package com.filesender;

import com.filesender.Cryptography.RSA;
import com.filesender.HelperClasses.globals;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class AppState {
    public static void changeToDisconnected(){
        globals.remoteIP = null;
        globals.previousDir = null;
        RSA.remoteKey = null;
        globals.connectButton.setText("Connect");
        globals.remoteTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("<No connection>")));
        globals.remoteIPTextField.setEditable(true);
        globals.remotePinTextField.setEditable(true);
    }

    public static void changeToConnected(){
        globals.connectButton.setText("Disconnect");
        globals.remoteIPTextField.setEditable(false);
        globals.remotePinTextField.setEditable(false);
    }
}
