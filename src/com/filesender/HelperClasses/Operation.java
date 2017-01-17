package com.filesender.HelperClasses;

/**
 * Created by arkadiusz.ryszewski on 17.01.2017.
 */

//   ID LIST
/*  0 - no task, just connection check
    1 - Rebuild tree for argument as a root
    2 - Send given file
    3 - Receive file of given name
    4 - Rename given file
    5 - accept public key from obj1 and send your public key as obj1
    6 - accept new public key from obj1
 */
public class Operation implements java.io.Serializable {
    public int opID;
    public String argument1;
    public String argument2;
    public Object obj1;
    public Operation(int _ID, String arg1,String arg2, Object _obj1) {
        opID = _ID;
        argument1 = arg1;
        argument2 = arg2;
        obj1 = _obj1;
    }
}
