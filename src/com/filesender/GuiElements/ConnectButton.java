package com.filesender.GuiElements;

import com.filesender.Connection;

import javax.swing.*;
import java.awt.*;

public class ConnectButton extends JButton {
    public ConnectButton(JTextField remoteIPTextField){
        super("Connect");
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.addActionListener(ae -> Connection.connectToRemote(remoteIPTextField));
    }
}
