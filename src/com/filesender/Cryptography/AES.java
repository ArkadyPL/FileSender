package com.filesender.Cryptography;

import com.filesender.HelperClasses.Log;
import com.filesender.HelperClasses.globals;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Class delivering all the methods necessary for using AES encryption in Java project.
 * All the methods and fields are of static type.
 * Before using, it is required to initialize class with method initialize().
 */
public class AES {
    /**
     * Symmetric key used for encryption/decryption. Is generated automatically when initialize().
     */
    public static SecretKey symmetricKey = null;
    /**
     * Cipher object to enable encryption/decryption. Is created when initialize().
     */
    private static Cipher cipher = null;

    /**
     * Static method that should be called before any usage of AES class for encoding/decoding.
     * Method generates the 'cipher' and 'symmetricKey' properties values for internal usage.
     */
    public static void initialize(){
        AES.generateCipher();
        AES.generateSymmetricKey();
    }

    /**
     * Static method for creating new symmetric key that will be saved as parameter 'symmetricKey' and used while encrypting/decrypting.
     * Is callled by initialize().
     *
     * @see AES#symmetricKey
     * @see AES#initialize()
     */
    public static void generateSymmetricKey(){
        KeyGenerator KeyGen = null;
        try { KeyGen = KeyGenerator.getInstance("AES"); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        KeyGen.init(128);
        AES.symmetricKey = KeyGen.generateKey();
        Log.WriteTerminal("New SymmetricKey: " + DatatypeConverter.printHexBinary(AES.symmetricKey.getEncoded()));
    }

    /**
     * Static method for generating new cipher. It is required at least once before using encryption/decryption. It is called by initialize().
     *
     * @see AES#initialize()
     */
    public static void generateCipher(){
        try { AES.cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) { e.printStackTrace(); }
    }

    /**
     * Static method for encrypting any kind of object of class inheriting after Object.
     *
     * @param object Object that is meant to be encrypted. Can be of any class inheriting after Object.
     *
     * @return Encrypted object in a form of byte[]. Can be read after decryption.
     *
     * @see AES#decrypt(byte[])
     */
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

    /**
     * Static method for decrypting object that was previously encrypted by encrypt().
     *
     * @param encryptedObject Object that is meant to be decrypted. Must have been previously encrypted with encrypt()
     *
     * @return Object of type Object. Must be casted to the proper class.
     *
     * @see AES#encrypt(Object)
     */
    public static Object decrypt(byte[] encryptedObject){
        if(AES.symmetricKey == null) return null;
        byte[] plainText = null;
        try { AES.cipher.init(Cipher.DECRYPT_MODE, AES.symmetricKey); }
        catch (InvalidKeyException e) { e.printStackTrace(); }

        try { plainText = AES.cipher.doFinal(encryptedObject); }
        catch (IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }

        return globals.toObject(plainText);
    }
}
