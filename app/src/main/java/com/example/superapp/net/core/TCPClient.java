package com.example.superapp.net.core;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TCPClient implements Client {

    private Socket socket;
    private BufferedOutputStream out;
    private BufferedInputStream in;
    private String hostname;
    private int port;

    private ErrorCallback errorCallback;
    private OnResponse onResponse;

    private Thread responseThread;
    private volatile AtomicBoolean responseThreadIsReady;
    private volatile AtomicBoolean connectionOpened;
    private final Object monitor = new Object();

    private int responseBuffSize = 1024;
    private int timeout = 1000;

    private final static String TAG = "TCPClient";

    private final Runnable runnable = () -> {
        responseThreadIsReady.set(true);
        synchronized (monitor) {
            monitor.notify();
        }
        while (connectionOpened.get()) {
            try {
                byte[] data = new byte[responseBuffSize];
                int n = in.read(data);
                onResponse.onResponse(data, n);
            } catch (SocketTimeoutException exception) {

            } catch (SocketException ignored) {

            } catch (IOException exception) {
                if (errorCallback != null) {
                    exception.printStackTrace();
                    errorCallback.onReadResponseError();
                }
            }
        }
    };

    public interface ErrorCallback {
        void onConnectionCloseError();
        void onConnectionOpenError();
        void onSendError();
        void onReadResponseError();
    }

    public interface OnResponse {
        void onResponse(byte[] data, int n);
        void onConnectionOpened();
        void onConnectionClosed();
    }

    public TCPClient(String hostname, int port, ErrorCallback errorCallback, OnResponse onResponse) {
        this.hostname = hostname;
        this.port = port;
        this.errorCallback = errorCallback;
        this.onResponse = onResponse;
    }

    public void openConnection() {
        closeConnection();
        Log.d(TAG, "openConnection() called");
        try {
            socket = new Socket(hostname, port);
            // socket.setSoTimeout(timeout);
            out = new BufferedOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());
            connectionOpened = new AtomicBoolean(true);

            responseThread = new Thread(runnable);
            responseThreadIsReady = new AtomicBoolean(false);
            responseThread.start();
            synchronized (monitor) {
                while (!responseThreadIsReady.get()) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            onResponse.onConnectionOpened();
        } catch (IOException exception) {
            exception.printStackTrace();
            errorCallback.onConnectionOpenError();
        }
    }

    public void send(byte[] data) {
        Log.d(TAG, "send() called");
        if (socket == null || socket.isClosed()) {
            errorCallback.onSendError();
            return;
        }
        try {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {

                        out.write(data);
                        out.flush();
                        //Your code goes here
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        } catch (Exception exception) {
            errorCallback.onSendError();
        }
    }

    public void closeConnection() {
        Log.d(TAG, "closeConnection() called");

        if (socket != null && !socket.isClosed()) {
            try {
                connectionOpened.set(false);
                socket.close();
                out.close();
                in.close();
                onResponse.onConnectionClosed();
            } catch (IOException exception) {
                errorCallback.onConnectionCloseError();
            } finally {
                socket = null;
                responseThread = null;
            }
        }
        socket = null;
        responseThread = null;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }


//    class MyAsyncTask extends AsyncTask<String, Void, Void> {
//
//        private Exception exception;
//
//        protected Void doInBackground(String... urls) {
//            System.out.println("urls[0]  " + urls[0]);
//            Log.d(TAG, "send() called");
//            if (socket == null || socket.isClosed()) {
//                errorCallback.onSendError();
//                return null;
//            }
//            try {
//                out.write(Integer.parseInt(urls[0]));
//                out.flush();
//            } catch (IOException exception) {
//                errorCallback.onSendError();
//            }
//        }
//
//        protected void onPostExecute(Void feed) {
//            // TODO: check this.exception
//            // TODO: do something with the feed
//        }
//    }
}
