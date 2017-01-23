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

/**
 * Class delivering all the methods necessary for using RSA encryption.
 * All the methods and fields are of static type.
 * Before using, it is required to initialize class with method initialize().
 */
public class RSA {
    /**
     * Helper static object necessary for the process of the encryption/decryption.
     * Initialized with method {@link #initialize()}.
     *
     * @see #initialize()
     */
    private static Cipher cipher = null;

    /**
     * Static property for storing public key that can be sent to all cooperating clients.
     * Initialized with method {@link #initialize()}.
     *
     * @see #initialize()
     */
    public static RSAPublicKey pubKey = null;

    /**
     * Static property for storing private key that we use for decrypting messages encrypted with our public key.
     * Initialized with method initialize().
     *
     * @see #initialize()
     */
    private static RSAPrivateKey privKey = null;

    /**
     * Static property for storing remote public key that we use for encrypting messages for remote systems with their public keys.
     * Initialized with method {@link #initialize()}.
     */
    public static RSAPublicKey remoteKey = null;

    /**
     * Static method used to initialize RSA static class before usage. It is required to do so at least once before encrypting/decrypting.
     */
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

    /**
     * Static method for RSA encrypting any kind of an object of class inheriting after the Object.
     *
     * @param object Object that is meant to be encrypted. Can be of any class inheriting after Object.
     *
     * @return Encrypted object in a form of byte[]. Can be read after decryption.
     *
     * @see #decrypt(byte[])
     */
    public static byte[] encrypt(Object object){
        byte[] plainText = globals.toByte(object);
        byte[] cipherText = null;
        try { RSA.cipher.init(Cipher.ENCRYPT_MODE, RSA.remoteKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { cipherText = RSA.cipher.doFinal(plainText); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return cipherText;
    }

    /**
     * Static method for RSA decrypting an object that was previously encrypted by {@link #encrypt(Object)}.
     *
     * @param encryptedObject Object that is meant to be decrypted. Must have been previously encrypted with {@link #encrypt(Object)}.
     *
     * @return Object of type Object. Must be casted to the proper class.
     *
     * @see #encrypt(Object)
     */
    public static Object decrypt(byte[] encryptedObject){
        byte[] plainText = null;
        try { RSA.cipher.init(Cipher.DECRYPT_MODE, RSA.privKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { plainText = RSA.cipher.doFinal(encryptedObject); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return globals.toObject(plainText);
    }
}
