package com.example.superapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.superapp.net.EndpointData;
import com.example.superapp.ui.PassportFragment;
import com.example.superapp.R;
import com.example.superapp.net.TCPQueueSender;
import com.example.superapp.net.core.TCPClient;
import com.example.superapp.ui.MyPagerAdapter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PagerActivity extends AppCompatActivity implements PassportFragment.ClickCallback {

    private static final String TAG = "PagerActivity";

    private TCPQueueSender tcpQueueSender;

    private String hostname;
    private int port;
    private boolean connectionOpened;
    private EndpointData eData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        eData = new EndpointData(this);
        hostname = eData.getHostname();
        port = eData.getPort();

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tcpQueueSender != null) {
            tcpQueueSender.closeConnection();
        }
        tcpQueueSender = new TCPQueueSender(hostname, port, errorCallback, onResponse);
        new Thread(() -> tcpQueueSender.openConnection()).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tcpQueueSender != null) {
            tcpQueueSender.closeConnection();
            connectionOpened = false;
        }
    }

    @Override
    public void clickPerformed(int fragmentId) {
        if (connectionOpened) {
            ByteBuffer b = ByteBuffer.allocate(1);
            b.put((byte) fragmentId);
            tcpQueueSender.send(b.array());
        }
    }

    private TCPClient.ErrorCallback errorCallback = new TCPClient.ErrorCallback() {
        @Override
        public void onConnectionCloseError() {

        }

        @Override
        public void onConnectionOpenError() {

        }

        @Override
        public void onSendError() {

        }

        @Override
        public void onReadResponseError() {

        }
    };

    private TCPClient.OnResponse onResponse = new TCPClient.OnResponse() {
        @Override
        public void onResponse(byte[] data, int n) {
            ByteBuffer b = ByteBuffer.wrap(data);
            b.order(ByteOrder.LITTLE_ENDIAN);
            float v = b.getFloat();
            System.out.println("RESULT!!!!!!!!!!!!!!!!!!!!!!!!!!!   " + v);
        }

        @Override
        public void onConnectionOpened() {
            connectionOpened = true;
        }

        @Override
        public void onConnectionClosed() {

        }
    };
}