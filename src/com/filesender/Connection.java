package com.filesender;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.Operation;
import com.filesender.HelperClasses.RSA;
import com.filesender.HelperClasses.globals;

import javax.crypto.KeyGenerator;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

public class Connection {

    public static void exchangeKeys() throws IOException {
        //Send our public key
        ObjectOutputStream ostream = new ObjectOutputStream(globals.connectionSocket.getOutputStream());
        Operation basicOperation = new Operation(5, null,null, globals.pubKey);
        ostream.writeObject(basicOperation);

        //Receive their public key
        ObjectInputStream inFromServer = new ObjectInputStream(globals.connectionSocket.getInputStream());
        try {
            globals.remoteKey = (RSAPublicKey) inFromServer.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.WriteTerminal("Remote PublicKey:\n" + DatatypeConverter.printHexBinary(globals.remoteKey.getEncoded()));

        //Create and send them our common symmetric key
        KeyGenerator KeyGen = null;
        try {
            KeyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyGen.init(128);
        globals.symmetricKey = KeyGen.generateKey();
        Log.WriteTerminal("SymmetricKey:\n" + DatatypeConverter.printHexBinary(globals.symmetricKey.getEncoded()));
        basicOperation = new Operation(6, null, null, globals.symmetricKey );
        ostream.writeObject(RSA.encrypt(basicOperation));

    }
}
