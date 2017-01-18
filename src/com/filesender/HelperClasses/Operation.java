package com.filesender.HelperClasses;

import com.filesender.Cryptography.AES;

//   ID LIST
/*  0 - no task, just connection check
    1 - Rebuild tree for argument as a root
    2 - Send given file
    3 - Receive file of given name
    4 - Rename given file
    5 - accept public key from obj1 and send your public key as obj1
    6 - accept symmetric key
 */
public class Operation implements java.io.Serializable {
    public int opID;
    public String argument1;
    public String argument2;
    public Object obj1;
    public byte[] argument1Encrypted;
    public byte[] argument2Encrypted;
    public byte[] obj1Encrypted;
    public Operation(int _ID, String arg1,String arg2, Object _obj1) {
        opID = _ID;
        argument1 = arg1;
        argument2 = arg2;
        obj1 = _obj1;
    }

    public Operation encryptFields(){
        if(globals.symmetricKey == null) return null;
        this.argument1Encrypted = AES.encrypt(this.argument1);
        this.argument1 = null;
        this.argument2Encrypted = AES.encrypt(this.argument2);
        this.argument2 = null;
        this.obj1Encrypted = AES.encrypt(this.obj1);
        this.obj1 = null;
        return this;
    }

    public Operation decryptFields(){
        if(globals.symmetricKey == null) return null;
        this.argument1 = (String)AES.decrypt(this.argument1Encrypted);
        this.argument1Encrypted = null;
        this.argument2 = (String)AES.decrypt(this.argument2Encrypted);
        this.argument2Encrypted = null;
        this.obj1 = AES.encrypt(this.obj1Encrypted);
        this.obj1Encrypted = null;
        return this;
    }
}
