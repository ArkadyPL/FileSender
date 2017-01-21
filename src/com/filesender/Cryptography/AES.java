package com.filesender.Cryptography;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AES {
    public static SecretKey symmetricKey = null;
    public static Cipher cipher = null;

    public static byte[] encrypt(Object object){
        if(AES.symmetricKey == null) return null;
        byte[] cipherText = null;
        byte[] plainText = globals.toByte(object);
        try { AES.cipher.init(Cipher.ENCRYPT_MODE, AES.symmetricKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { cipherText = AES.cipher.doFinal(plainText); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return cipherText;
    }

    public static Object decrypt(byte[] encryptedObject){
        if(AES.symmetricKey == null) return null;
        byte[] plainText = null;
        try { AES.cipher.init(Cipher.DECRYPT_MODE, AES.symmetricKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { plainText = AES.cipher.doFinal(encryptedObject); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return globals.toObject(plainText);
    }

    public static void generateSymmetricKey(){
        KeyGenerator KeyGen = null;
        try { KeyGen = KeyGenerator.getInstance("AES"); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        KeyGen.init(128);
        AES.symmetricKey = KeyGen.generateKey();
        Log.WriteTerminal("SymmetricKey:\n" + DatatypeConverter.printHexBinary(AES.symmetricKey.getEncoded()));
    }

    public static void generateCipher(){
        try { AES.cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) { e.printStackTrace(); }
    }

    public static void initialize(){
        AES.generateCipher();
        AES.generateSymmetricKey();
    }
}
