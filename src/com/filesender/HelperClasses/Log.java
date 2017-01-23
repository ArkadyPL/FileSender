package com.filesender.HelperClasses;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public static JTextArea textArea = new JTextArea();

    /**
     * Method for displaying log message to terminal only
     *
     * @param text - text to be displayed
     */
    public static void WriteTerminal(String text){
        System.out.println(text);
    }

    /**
     * Method for displaying log message to both, terminal and logTextArea
     *
     * @param text - text to be displayed
     */
    public static void Write(String text){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        System.out.println(text);
        String logText = textArea.getText();
        textArea.setText(logText + "[" + dateFormat.format(date) + "] " + text + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }
}
