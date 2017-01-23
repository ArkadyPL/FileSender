package com.filesender.HelperClasses;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for displaying output in FileSender application.
 */
public class Log {
    /**
     * JTextArea that is displayed in the bottom of main frame of FileSender app.
     * Log messages will appear there.
     */
    public static JTextArea textArea = new JTextArea();

    /**
     * Method for displaying log messages to the terminal only.
     *
     * @param text - Text to be displayed.
     */
    public static void WriteTerminal(String text){
        System.out.println(text);
    }

    /**
     * Method for displaying log messages to both, the terminal and the {@link #textArea}.
     *
     * @param text - Text to be displayed.
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
