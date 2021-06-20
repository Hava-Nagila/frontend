package com.example.superapp.net;

import android.util.Log;

import com.example.superapp.net.core.QueueSender;
import com.example.superapp.net.core.TCPClient;
import com.example.superapp.net.core.Sender;

import java.util.concurrent.LinkedBlockingDeque;

public class TCPQueueSender extends QueueSender {

    private TCPClient socket;

    private final static String TAG = "TCPQueueSender";

    public TCPQueueSender(String hostname,
                          int port,
                          TCPClient.ErrorCallback errorCallback,
                          TCPClient.OnResponse onResponse)
    {
        super();
        socket = new TCPClient(hostname, port, errorCallback, onResponse);
    }

    public void openConnection() {
        socket.openConnection();
    }

    public void closeConnection() {
        socket.closeConnection();
    }

    @Override
    public void send(byte[] data) {
        long t1 = System.currentTimeMillis();
        socket.send(data);
        long t2 = System.currentTimeMillis();
        Log.d(TAG, "" + data.length + " bytes sent in " + (t2 - t1) );
    }
}
