package com.example.harshith.chatsockets;

import android.content.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by harshith on 5/1/17.
 */

public class SendDataThread extends Thread{
    Socket socket;
    private OutputStream outputStream;
    Context context;
    ArrayList<String> request;
    public SendDataThread(Socket socket, Context context, ArrayList<String> request) {
        this.socket = socket;
        this.context = context;
        this.request = request;
    }

    @Override
    public void run() {
        super.run();
        if(request != null) {
            if(socket != null) {
                if (socket.isConnected()) {
                    try {
                        outputStream = socket.getOutputStream();
                        byte[] messageBytes = Utils.list2string(request).getBytes();
                        outputStream.write(messageBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
