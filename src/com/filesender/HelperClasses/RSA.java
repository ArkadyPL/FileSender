package com.filesender.HelperClasses;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.security.InvalidKeyException;

public class RSA {
    public static byte[] encrypt(Object object){
        byte[] plainText = globals.ToByte(object);
        byte[] cipherText = null;
        try {
            globals.cipher.init(Cipher.ENCRYPT_MODE, globals.remoteKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            cipherText = globals.cipher.doFinal(plainText);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static Object decrypt(byte[] encryptedObject){
        try {
            globals.cipher.init(Cipher.DECRYPT_MODE, globals.privKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] plainText = new byte[encryptedObject.length*8];
        try {
            plainText = globals.cipher.doFinal(encryptedObject);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return globals.ToObject(plainText);
    }
}
