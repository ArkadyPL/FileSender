package com.filesender.Cryptography;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSA {
    public static Cipher cipher = null;
    public static RSAPublicKey pubKey = null;
    public static RSAPrivateKey privKey = null;
    public static RSAPublicKey remoteKey = null;

    public static byte[] encrypt(Object object){
        byte[] plainText = globals.toByte(object);
        byte[] cipherText = null;
        try { RSA.cipher.init(Cipher.ENCRYPT_MODE, RSA.remoteKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { cipherText = RSA.cipher.doFinal(plainText); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return cipherText;
    }

    public static Object decrypt(byte[] encryptedObject){
        byte[] plainText = null;
        try { RSA.cipher.init(Cipher.DECRYPT_MODE, RSA.privKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { plainText = RSA.cipher.doFinal(encryptedObject); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return globals.toObject(plainText);
    }

    public static void initialize() {
        KeyPairGenerator kpg = null;
        try {
            RSA.cipher = Cipher.getInstance("RSA");
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        RSA.pubKey = (RSAPublicKey) kp.getPublic();
        RSA.privKey = (RSAPrivateKey) kp.getPrivate();
        Log.WriteTerminal("Local PublicKey:\n" + DatatypeConverter.printHexBinary(RSA.pubKey.getEncoded()));
    }
}
