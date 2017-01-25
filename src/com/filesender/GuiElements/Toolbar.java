package com.filesender.GuiElements;

import com.filesender.HelperClasses.globals;

import javax.swing.*;
import java.awt.*;

/**
 * Class extending JToolbar for needs of FileSender app. It hides all the 'dirty stuff' from main function and creates fully functioning toolbar
 * which meets the needs of the application.
 * @see javax.swing.JToolBar
 */
public class Toolbar extends JToolBar{

    /**
     * Constructor method creating application toolbar with desired appearance and values.
     *
     * @param localIP Value that is to be displayed in the toolbar as 'local IP'
     */
    public Toolbar(String localIP){
        super();
        this.setFloatable(false);

        //Left panel is place that contains our local IP and PIN values and allows to copy them.
        //It is placed on the left side of the toolbar
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
        JTextField localPinTextField = new JTextField(globals.localPIN);
        localPinTextField.setEditable(false);
        localPinTextField.setBorder(null);
        localPinTextField.setForeground(UIManager.getColor("Label.foreground"));
        localPinTextField.setFont(UIManager.getFont("Label.font"));
        leftPanel.add(localPinTextField);

        this.add(leftPanel);
        this.add(new ConnectionPanel());
    }
}
