package com.example.superapp.net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.superapp.R;

public class EndpointData {

    private String hostname;
    private int port;
    private int code;

    private static final String TAG = "EndpointData";

    public EndpointData(Context context) {
        Resources res = context.getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences != null) {
            hostname = sharedPreferences.getString(
                    res.getString(R.string.host),
                    res.getString(R.string.hostDefault)
            );
            port = Integer.parseInt(sharedPreferences.getString(
                    res.getString(R.string.port),
                    res.getString(R.string.portDefault)
            ));
//            code = Integer.parseInt(sharedPreferences.getString(
//                    res.getString(R.string.settings_event),
//                    res.getString(R.string.settings_event_default_value)
//            ));
       }
//        Log.d(TAG, "Hostname: " + hostname + " Port: " + port);
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public int getCode() {
        return code;
    }
}
