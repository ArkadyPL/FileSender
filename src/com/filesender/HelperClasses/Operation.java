package com.filesender.HelperClasses;

import com.filesender.Cryptography.AES;

/**
 * Objects of this class are meant to be sent as operation indicators for server with possible arguments.
 */
public class Operation implements java.io.Serializable {
    /**
     * Property indicating what kind of operation is meant to take place.
     * ID number meaning:
     * <p>0 - no task or just connection check</p>
     * <p>1 - Rebuild tree for argument as a root</p>
     * <p>2 - Send given file</p>
     * <p>3 - Receive file of given name</p>
     * <p>4 - Rename given file</p>
     * <p>5 - accept public key</p>
     * <p>6 - accept symmetric key</p>
     */
    public int opID;

    /**
     * Property used for string unencrypted version of the argument 1.
     */
    public String argument1;

    /**
     * Property used for string encrypted version of the argument 1.
     */
    public byte[] argument1Encrypted;

    /**
     * Property used for string unencrypted version of the argument 2.
     */
    public String argument2;

    /**
     * Property used for string encrypted version of the argument 2.
     */
    public byte[] argument2Encrypted;

    /**
     * Property used for string unencrypted version of the object.
     */
    public Object obj1;

    /**
     * Property used for string encrypted version of the object.
     */
    public byte[] obj1Encrypted;



    /**
     * Constructor creating new Operation object with given values.
     * @param _ID New id's value
     * @param arg1 New argument1's value
     * @param arg2 New argument2's value
     * @param _obj1 New obj1's value
     */
    public Operation(int _ID, String arg1,String arg2, Object _obj1) {
        opID = _ID;
        argument1 = arg1;
        argument2 = arg2;
        obj1 = _obj1;
    }

    /**
     * Method that encrypts all the properties' values except ID.
     * All encrypted values are saved in appropriate alternative fields with postfix 'Encrypted'.
     * Normal fields' values are set to null.
     * @return The operation object itself but with encrypted fields
     */
    public Operation encryptFields(){
        if(AES.symmetricKey == null) return null;
        this.argument1Encrypted = AES.encrypt(this.argument1);
        this.argument1 = null;
        this.argument2Encrypted = AES.encrypt(this.argument2);
        this.argument2 = null;
        this.obj1Encrypted = AES.encrypt(this.obj1);
        this.obj1 = null;
        return this;
    }

    /**
     * Method that decrypts all the properties' values except ID.
     * All decrypted values are saved in appropriate fields without postfix 'Encrypted'.
     * Values of fields with postfix 'Encrypted' are set to null.
     * @return The operation object itself but with decrypted fields
     */
    public Operation decryptFields(){
        if(AES.symmetricKey == null) return null;
        this.argument1 = (String)AES.decrypt(this.argument1Encrypted);
        this.argument1Encrypted = null;
        this.argument2 = (String)AES.decrypt(this.argument2Encrypted);
        this.argument2Encrypted = null;
        this.obj1 = AES.decrypt(this.obj1Encrypted);
        this.obj1Encrypted = null;
        return this;
    }
}
