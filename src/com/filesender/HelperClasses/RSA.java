package com.filesender.HelperClasses;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.security.InvalidKeyException;

public class RSA {
    public static byte[] encrypt(Object obj){
        byte[] encryptedObject = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            encryptedObject = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        byte[] cipherText = new byte[0];
        try {
            globals.cipher.init(Cipher.ENCRYPT_MODE, globals.remoteKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            cipherText = globals.cipher.doFinal(encryptedObject);
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
        byte[] plainText = new byte[0];
        try {
            plainText = globals.cipher.doFinal(encryptedObject);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(plainText);
        ObjectInput in = null;
        Object decryptedObject = null;
        try {
            in = new ObjectInputStream(bis);
            decryptedObject = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return decryptedObject;
    }
}
