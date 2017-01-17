package com.filesender.HelperClasses;

public class ToSend  implements java.io.Serializable {
    public Object node;
    public Boolean isRoot;
    public ToSend(Object _node, Boolean is) {
        node = _node;
        isRoot = is;
    }
}
