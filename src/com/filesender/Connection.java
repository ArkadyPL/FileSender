package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class Connection {

    public static void exchangeKeys() throws IOException, ClassNotFoundException, InterruptedException {
        ObjectOutputStream ostream = new ObjectOutputStream(globals.connectionSocket.getOutputStream());
        operation basicOperation = new operation(5, null,null, globals.privKey);
        ostream.writeObject(basicOperation);
        ObjectInputStream inFromServer = new ObjectInputStream(globals.connectionSocket.getInputStream());
        operation basicOp = null;
        try {
            basicOp = (operation)inFromServer.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        globals.remoteKey = (RSAPrivateKey)basicOp.obj1;
        Log.Write("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));
    }

}
