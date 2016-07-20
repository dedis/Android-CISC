package com.epfl.dedis.cisc;

public enum Log {
    LOG("LOG"),
    HOST("HOST"),
    PORT("PORT"),
    DATA("DATA"),
    ID("ID"),
    PUBLIC("PUBLIC"),
    SECRET("SECRET");

    private String key;

    Log(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
