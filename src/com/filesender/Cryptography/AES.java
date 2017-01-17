package com.filesender.Cryptography;

import com.filesender.HelperClasses.globals;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

/**
 * Created by arkadiusz.ryszewski on 18.01.2017.
 */
public class AES {
    public static byte[] encrypt(Object object){
        byte[] cipherText = null;
        byte[] plainText = globals.toByte(object);
        try {
            globals.aesCipher.init(Cipher.ENCRYPT_MODE, globals.symmetricKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            cipherText = globals.aesCipher.doFinal(plainText);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static Object decrypt(byte[] encryptedObject){
        byte[] plainText = null;
        try {
            globals.aesCipher.init(Cipher.DECRYPT_MODE, globals.symmetricKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            plainText = globals.aesCipher.doFinal(encryptedObject);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return globals.toObject(plainText);
    }
}
