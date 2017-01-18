package com.filesender.GuiElements;

import com.filesender.Connection;
import com.filesender.HelperClasses.IPAddressValidator;
import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.ServerStatus;
import com.filesender.HelperClasses.globals;
import com.filesender.Receiver;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectButton extends JButton {
    public ConnectButton(JTextField remoteIPTextField){
        super("Connect");
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.addActionListener(ae -> Connection.connectToRemote(remoteIPTextField));
    }
}
