package com.filesender.GuiElements;

import com.filesender.Connection;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConnectionPanel extends JPanel {

    public ConnectionPanel(){
        super();
        this.add(new JLabel("Remote IP:"));
        JTextField remoteIPTextField = new JTextField(30);
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Connection.connectToRemote(remoteIPTextField);
            }
        };
        remoteIPTextField.addActionListener( action );
        this.add(remoteIPTextField);
        this.add(new ConnectButton(remoteIPTextField));
    }
}
