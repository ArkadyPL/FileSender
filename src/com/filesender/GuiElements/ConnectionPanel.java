package com.filesender.GuiElements;

import com.filesender.Connection;
import com.filesender.HelperClasses.globals;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class extending JPanel for needs of FileSender app.
 * It is meant to be displayed on the right part of the toolbar.
 * @see javax.swing.JPanel
 */
public class ConnectionPanel extends JPanel {

    /**
     * Constructor creating the panel. Adds the button and 'enter-clicked' action listeners method: {@link Connection#connectToRemote()}.
     * @see Connection#connectToRemote()
     */
    public ConnectionPanel() {
        super();
        this.add(new JLabel("Remote IP:"));
        this.add(globals.remoteIPTextField);

        this.add(new JLabel("PIN:"));

        this.add(globals.remotePinTextField);

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {Connection.connectToRemote();
            }
        };
        globals.remoteIPTextField.addActionListener(action);
        globals.remotePinTextField.addActionListener(action);

        globals.connectButton = new ConnectButton();
        this.add(globals.connectButton);
    }
}
