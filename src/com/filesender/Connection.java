package com.filesender;

import com.filesender.HelperClasses.Operation;
import com.filesender.HelperClasses.globals;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Connection {

    public static void exchangeKeys() throws IOException {
        ObjectOutputStream ostream = new ObjectOutputStream(globals.connectionSocket.getOutputStream());
        Operation basicOperation = new Operation(5, null,null, globals.pubKey);
        ostream.writeObject(basicOperation);

        System.out.println("Before:\n" + DatatypeConverter.printHexBinary(globals.pubKey.getEncoded()));

    }
}
