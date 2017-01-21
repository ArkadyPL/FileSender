package com.filesender.GuiElements;

import com.filesender.HelperClasses.globals;

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JToolBar{
    public Toolbar(String localIP){
        super();
        this.setFloatable(false);
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2,2));
        leftPanel.add(new JLabel("Your IP address is: "));
        //We use JTextField to enable selecting IP with a mouse
        JTextField localIpTextField = new JTextField(localIP);
        localIpTextField.setEditable(false);
        localIpTextField.setBorder(null);
        localIpTextField.setForeground(UIManager.getColor("Label.foreground"));
        localIpTextField.setFont(UIManager.getFont("Label.font"));
        leftPanel.add(localIpTextField);

        leftPanel.add(new JLabel("Your PIN is: "));
        //We use JTextField to enable selecting PIN with a mouse
        JTextField localPinTextField = new JTextField(globals.generatePIN());
        localPinTextField.setEditable(false);
        localPinTextField.setBorder(null);
        localPinTextField.setForeground(UIManager.getColor("Label.foreground"));
        localPinTextField.setFont(UIManager.getFont("Label.font"));
        leftPanel.add(localPinTextField);


        this.add(leftPanel);
        this.add(new ConnectionPanel());
    }
}
