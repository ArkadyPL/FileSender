package com.filesender.Cryptography;

import com.filesender.HelperClasses.globals;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

public class RSA {
    public static byte[] encrypt(Object object){
        byte[] plainText = globals.toByte(object);
        byte[] cipherText = null;
        try { globals.cipher.init(Cipher.ENCRYPT_MODE, globals.remoteKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { cipherText = globals.cipher.doFinal(plainText); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return cipherText;
    }

    public static Object decrypt(byte[] encryptedObject){
        byte[] plainText = null;
        try { globals.cipher.init(Cipher.DECRYPT_MODE, globals.privKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { plainText = globals.cipher.doFinal(encryptedObject); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return globals.toObject(plainText);
    }
}
