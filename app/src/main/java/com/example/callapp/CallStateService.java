package com.example.callapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class CallStateService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        initPhoneStateListener();
        initPhoneCallView
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
