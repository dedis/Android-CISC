package com.epfl.dedis.net;

public abstract class Message {
    private int type;

    public Message(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
