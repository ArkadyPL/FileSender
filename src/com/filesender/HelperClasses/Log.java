package com.filesender.HelperClasses;

import javax.swing.text.DefaultCaret;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arkadiusz.ryszewski on 16.01.2017.
 */
public class Log {
    /**
     * Method for displaying log message to terminal only
     *
     * @author Arkadiusz Ryszewski
     * @param text - text to be displayed
     */
    public static void WriteTerminal(String text){
        System.out.println(text);
    }

    /**
     * Method for displaying log message to both, terminal and logTextArea
     *
     * @author Arkadiusz Ryszewski
     * @param text - text to be displayed
     */
    public static void Write(String text){

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        System.out.println(text);
        String logText = globals.logTextArea.getText();
        globals.logTextArea.setText( "[" + dateFormat.format(date) + "] " + text + "\n" + logText);
        globals.logTextArea.append("\n");
        globals.logTextArea.setCaretPosition(0);
    }
}
