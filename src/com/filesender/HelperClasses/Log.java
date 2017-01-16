package com.filesender.HelperClasses;

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
        System.out.println(text);
        String logText = globals.logTextArea.getText();
        globals.logTextArea.setText(logText + "\n" + text);
    }
}
