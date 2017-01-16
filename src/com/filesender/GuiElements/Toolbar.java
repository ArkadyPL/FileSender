package com.filesender.GuiElements;

import javax.swing.*;

/**
 * Created by arkadiusz.ryszewski on 16.01.2017.
 */
public class Toolbar extends JToolBar{
    public Toolbar(String localIP){
        super();
        this.setFloatable(false);
        this.add(new JLabel("Your IP address is: "));

        //We use JTextField to enable selecting IP with a mouse
        JTextField localITextField = new JTextField(localIP);
        localITextField.setEditable(false);
        localITextField.setBorder(null);
        localITextField.setForeground(UIManager.getColor("Label.foreground"));
        localITextField.setFont(UIManager.getFont("Label.font"));
        this.add(localITextField);
        this.add(new ConnectionPanel());
    }
}
