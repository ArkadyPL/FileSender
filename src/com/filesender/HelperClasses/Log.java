package com.filesender.HelperClasses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
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
        String logText = globals.logTextArea.getText();
        globals.logTextArea.setText(logText + "[" + dateFormat.format(date) + "] " + text + "\n");
        globals.logTextArea.setCaretPosition(globals.logTextArea.getText().length());
    }
}
