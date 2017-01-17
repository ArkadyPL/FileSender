package com.filesender.GuiElements;

import javax.swing.*;

public class ConnectionPanel extends JPanel {

    public ConnectionPanel(){
        super();
        this.add(new JLabel("Remote IP:"));
        JTextField remoteIPTextField = new JTextField(30);
        this.add(remoteIPTextField);
        this.add(new ConnectButton(remoteIPTextField));
    }
}
