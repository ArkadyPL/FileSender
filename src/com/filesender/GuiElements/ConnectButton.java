package com.filesender.GuiElements;

import com.filesender.Connection;

import javax.swing.*;
import java.awt.*;

/**
 * Class extending JButton for needs of FileSender app.
 * @see javax.swing.JButton
 */
public class ConnectButton extends JButton {

    /**
     * Constructor creating button and adding it proper properties and onclick listener method: {@link Connection#connectToRemote(JTextField, JTextField)}.
     * @param remoteIPTextField Field that action listener method will fetch IP value from.
     * @param remotePinTextField Field that action listener method will fetch PIN value from.
     */
    public ConnectButton(JTextField remoteIPTextField, JTextField remotePinTextField){
        super("Connect");
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.addActionListener(ae -> Connection.connectToRemote(remoteIPTextField, remotePinTextField));
    }
}
