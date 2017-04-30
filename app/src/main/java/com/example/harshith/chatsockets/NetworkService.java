package com.example.harshith.chatsockets;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkService extends Service {
    public NetworkService() {
    }

    InputStream inputStream;
    OutputStream outputStream;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        ArrayList<String> request = intent.getStringArrayListExtra(Constants.REQUEST);

        boolean wasSocketOpen = true;
        String ipAddress = intent.getStringExtra(Constants.IP_ADDR);
        int port = intent.getIntExtra(Constants.PORT, 9399);
        if(SocketHandler.getSocket() == null) {
            wasSocketOpen = false;
            try {
                InetAddress inetAddress = Inet4Address.getByName(ipAddress);
                SocketHandler.setSocket(new Socket(inetAddress, port));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (!SocketHandler.getSocket().isConnected()) {
                wasSocketOpen = false;
                InetAddress inetAddress = null;
                try {
                    inetAddress = Inet4Address.getByName(ipAddress);
                    SocketHandler.setSocket(new Socket(inetAddress, port));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(request.get(1).equals("login")) {
            SendDataThread sendDataThread = new SendDataThread(SocketHandler.getSocket(), getApplicationContext(), request);
            sendDataThread.start();

            ReceiveDataThread receiveDataThread = new ReceiveDataThread(SocketHandler.getSocket(), getApplicationContext());
            receiveDataThread.start();
        }
        else {
            SendDataThread sendDataThread = new SendDataThread(SocketHandler.getSocket(), getApplicationContext(), request);
            sendDataThread.start();
            if(!wasSocketOpen) {
                ReceiveDataThread receiveDataThread = new ReceiveDataThread(SocketHandler.getSocket(), getApplicationContext());
                receiveDataThread.start();
            }
        }



        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
