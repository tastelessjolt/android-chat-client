package com.example.harshith.chatsockets;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NetworkService extends Service {
    public NetworkService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
