package com.filesender;

import static com.filesender.FileSender.isListening;

/**
 * Created by arkadiusz.ryszewski on 10.01.2017.
 */
public class ConnectionListener {
        public static String ListenForIncomingConnections(){
            System.out.println("Waiting for incoming connections...");
            while(true){
                if(isListening == false) break;
            }
            System.out.println("Not waiting for incoming connections.");
            return null;
        }
}
