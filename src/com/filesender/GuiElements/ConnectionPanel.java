package com.filesender.GuiElements;

import com.filesender.Connection;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConnectionPanel extends JPanel {

    public ConnectionPanel() {
        super();
        this.add(new JLabel("Remote IP:"));
        JTextField remoteIPTextField = new JTextField(20);
        this.add(remoteIPTextField);

        this.add(new JLabel("PIN:"));
        JTextField remotePinTextField = new JTextField(4);
        this.add(remotePinTextField);

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection.connectToRemote(remoteIPTextField, remotePinTextField);
            }
        };
        remoteIPTextField.addActionListener(action);
        remotePinTextField.addActionListener(action);

        this.add(new ConnectButton(remoteIPTextField, remotePinTextField));
    }
}
