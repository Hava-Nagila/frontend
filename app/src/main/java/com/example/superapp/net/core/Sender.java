package com.example.superapp.net.core;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Sender {

    private Thread sendThread;
    protected volatile AtomicBoolean hasData;
    private volatile AtomicBoolean running;

    private static final String TAG = "Sender";

    public Sender() {
        hasData = new AtomicBoolean(false);
        running = new AtomicBoolean(true);
        sendThread = new Thread(runnable);
        sendThread.start();
    }

    private final Runnable runnable = () -> {
        while (running.get()) {
            while (!hasData.get()) {
                synchronized (sendThread) {
                    try {
                        sendThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            send(getData());
            hasData.set(updateDataState());
        }
    };

    public final void notifySender() {
        hasData.set(true);
        synchronized (sendThread) {
            sendThread.notify();
        }
    }

    public final void stop() {
        running.set(false);
    }

    protected abstract boolean updateDataState();

    protected abstract byte[] getData();

    public abstract void send(byte[] data);
}
