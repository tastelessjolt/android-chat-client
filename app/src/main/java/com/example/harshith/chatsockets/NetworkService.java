package com.example.harshith.chatsockets;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

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

    private static BroadcastReceiver broadcastReceiver;

    public static BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    public static void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        NetworkService.broadcastReceiver = broadcastReceiver;
    }

    InputStream inputStream;
    OutputStream outputStream;
    public Handler handler;

    ArrayList<Person> online, friends, allusers;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message message) {
                if(message.what == Constants.RESPONSE) {
                    Bundle bundle = message.getData();
                    if(bundle != null) {
                        ArrayList<String> data = bundle.getStringArrayList(Constants.READ);
                        if (data.get(0).equals("login")) {
                            // Correct Username + Password. Log in
                        }
                        else if(data.get(0).equals("wrong")) {
                            // Username/Password incorrect
                        }
                        else if(data.get(0).equals("logout")) {
                            // Username/Password incorrect
                        }
                        else if(data.get(0).equals("olusers")) {
                            online  = Person.getPersonArray(data);
                        }
                        else if(data.get(0).equals("users")) {
                            allusers = Person.getPersonArray(data);
                            if(online != null && friends != null && allusers != null)
                            Database.initialize(allusers, friends, online);
                        }
                        else if(data.get(0).equals("urfriends")) {
                            friends = Person.getPersonArray(data);
                        }
                        else if(data.get(0).equals("online")) {
                            Database.setOnline(data);
                        }
                        else if(data.get(0).equals("offline")) {
                            Database.setOffline(data);
                        }
                        else if(data.get(0).equals("message")) {
                            Database.getPerson(data.get(1)).addMessage(data.get(2), false);
                        }
                        else if(data.get(0).equals("blocked")) {
                            Person blocked;
                            blocked = Database.getPerson(data.get(1));
                            blocked.setFriendIndication(2);
                        }
                        else if(data.get(0).equals("sentreq")) {
                            Database.getPerson(data.get(1)).setFriendIndication(1);
                        }
                        else if(data.get(0).equals("recvreq")) {
                            Database.getPerson(data.get(1)).setFriendIndication(-1);
                        }
                        else if(data.get(0).equals("accepted")) {
                            Database.getPerson(data.get(1)).setFriendIndication(0);
                        }
                        else if(data.get(0).equals("acceptedyour")) {
                            Database.getPerson(data.get(1)).setFriendIndication(0);
                        }
                        else {
                            //
                        }
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        setBroadcastReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Broadcast received for receiving thread");

//                ReceiveDataThread receiveDataThread = new ReceiveDataThread(SocketHandler.getSocket(), context, ipAdd);
//                receiveDataThread.start();
            }
        });

        ArrayList<String> request = intent.getStringArrayListExtra(Constants.REQUEST);

        boolean wasSocketOpen = true;
        String ipAddress = intent.getStringExtra(Constants.IP_ADDR);
        int port = intent.getIntExtra(Constants.PORT, 9399);
        System.out.println(request);


        if(request.get(0).equals("login")) {
            SendDataThread sendDataThread = new SendDataThread(SocketHandler.getSocket(), getApplicationContext(), request, ipAddress, port);
            sendDataThread.start();
            System.out.println("start Senddatathread");
            ReceiveDataThread receiveDataThread = new ReceiveDataThread(SocketHandler.getSocket(), getApplicationContext(), , ipAddress, port);
            receiveDataThread.start();
            System.out.println("start receivedatathread");
        }
        else {
            SendDataThread sendDataThread = new SendDataThread(SocketHandler.getSocket(), getApplicationContext(), request, ipAddress, port);
            sendDataThread.start();
            System.out.println("start Senddatathread");
//            if(!wasSocketOpen) {
//                ReceiveDataThread receiveDataThread = new ReceiveDataThread(SocketHandler.getSocket(), getApplicationContext());
//                receiveDataThread.start();
//            }
        }



        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
