package com.filesender.HelperClasses;

/**
 * Created by arkadiusz.ryszewski on 17.01.2017.
 */
public class ToSend  implements java.io.Serializable {
    public Object node;
    public Boolean isRoot;
    public ToSend(Object _node, Boolean is) {
        node = _node;
        isRoot = is;
    }
}
