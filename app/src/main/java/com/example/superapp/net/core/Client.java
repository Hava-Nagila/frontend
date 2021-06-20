package com.example.superapp.net.core;

public interface Client {

    void openConnection();
    void closeConnection();
    void send(byte[] data);
    boolean isConnected();
}
