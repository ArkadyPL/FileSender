package com.filesender.GuiElements;

import com.filesender.HelperClasses.IPAddressValidator;
import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;
import com.filesender.Receiver;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by arkadiusz.ryszewski on 16.01.2017.
 */
public class ConnectButton extends JButton {
    public ConnectButton(JTextField remoteIPTextField){
        super("Connect");
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.addActionListener(ae -> {
            try {
                globals.remoteIP = InetAddress.getByName(remoteIPTextField.getText());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            Boolean isValid = new IPAddressValidator().validate(remoteIPTextField.getText());
            Log.WriteTerminal("Connection button clicked. Remote IP value: " + globals.remoteIP  + "\tGiven IP address is " + (isValid ? "valid" : "not valid"));
            if(isValid) {
                try {
                    Log.Write("Trying to connect to remote server...");
                    globals.connectionSocket = new Socket(globals.remoteIP, 9990);
                } catch (ConnectException e) {
                    Log.Write("Remote is not available...");
                } catch (Exception e) {
                    Log.Write("Connection error...");
                }
                if (globals.connectionSocket != null) {
                    globals.isConnected = true;
                    Log.Write("Connected to remote!");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Receiver.buildRemoteTree(globals.remoteTree,globals.connectionSocket,"root",false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    globals.frame.repaint();
                    globals.frame.revalidate();
                }
            }
        });
    }
}
