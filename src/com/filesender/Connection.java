package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.HelperClasses.globals;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.interfaces.RSAPublicKey;

public class Connection {

    public static void exchangeKeys() throws IOException {
        ObjectOutputStream ostream = new ObjectOutputStream(globals.connectionSocket.getOutputStream());
        Operation basicOperation = new Operation(5, null,null, globals.pubKey);
        ostream.writeObject(basicOperation);

        ObjectInputStream inFromServer = new ObjectInputStream(globals.connectionSocket.getInputStream());
        Operation basicOp = null;
        try {
            basicOp = (Operation)inFromServer.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        globals.remoteKey = (RSAPublicKey)basicOp.obj1;
        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));
    }
}
