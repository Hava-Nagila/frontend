package com.example.superapp.net.core;

import android.util.Log;

import java.util.concurrent.LinkedBlockingDeque;

public abstract class QueueSender extends Sender {

    private TCPClient socket;
    private LinkedBlockingDeque<byte[]> dataQueue;

    private static final int DATA_QUEUE_SIZE = 3;

    private final static String TAG = "QueueSender";

    public QueueSender()
    {
        super();
        dataQueue = new LinkedBlockingDeque<>(DATA_QUEUE_SIZE);
    }

    public final void addData(byte[] data) {
        if (dataQueue.size() == DATA_QUEUE_SIZE) {
            dataQueue.removeFirst();
        }
        dataQueue.add(data);
        if (!hasData.get()) {
            notifySender();
        }
    }

    @Override
    protected final boolean updateDataState() {
        return dataQueue.size() != 0;
    }

    @Override
    protected final byte[] getData() {
        return dataQueue.removeFirst();
    }
}
