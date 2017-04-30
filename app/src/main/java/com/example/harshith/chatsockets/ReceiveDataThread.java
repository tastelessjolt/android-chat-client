package com.example.harshith.chatsockets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by harshith on 5/1/17.
 */

public class ReceiveDataThread extends Thread{
    private Socket socket;
    private InputStream inputStream;
    Context context;
    public ReceiveDataThread(Socket socket, Context context) {
        this.socket = socket;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        if(socket != null) {
            try {
                inputStream = socket.getInputStream();
                if (inputStream != null) {
                    byte[] buffer = new byte[256];
                    int endPos = 0;
                    ArrayList<String> data;
                    StringBuilder stringBuilder = new StringBuilder();
                    while(true) {
                        // reading login ack
                        int bytes = inputStream.read(buffer);
                        if(bytes == -1) {
                            // throw error
                        }
                        stringBuilder.append(new String(buffer).substring(0, bytes));
                        Log.d("stream", stringBuilder.toString());

                        while((endPos = stringBuilder.indexOf("#")) < 0) {
                            bytes = inputStream.read(buffer);
                            if (bytes == -1) {
                                // throw error
                            } else {
                                stringBuilder.append(new String(buffer).substring(0, bytes));
                                Log.d("stream", stringBuilder.toString());
                            }
                        }

                        data = Utils.string2list(stringBuilder.substring(0, endPos + 1));
                        System.out.println(data);
                        if(data.get(0).equals("wrong") || data.get(0).equals("login")) {
                            if(LoginActivity.getBroadcastReceiver() != null) {
                                LocalBroadcastManager.getInstance(this.context).registerReceiver(LoginActivity.getBroadcastReceiver(), new IntentFilter(Constants.BROADCAST));
                                Intent intent = new Intent(Constants.BROADCAST);
                                intent.putExtra(Constants.BROADCAST, data);
                                context.sendBroadcast(intent);

                            }
                            else if(NetworkService.getBroadcastReceiver() != null) {
                                LocalBroadcastManager.getInstance(this.context).registerReceiver(NetworkService.getBroadcastReceiver(), new IntentFilter(Constants.BROADCAST));

                            }
                        }
                        else if(data.get(0).equals("message")) {

                        }
                        stringBuilder.delete(0, endPos + 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
