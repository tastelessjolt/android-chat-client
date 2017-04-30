package com.example.harshith.chatsockets;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
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
    String ipAddr;
    int port;
    public SendDataThread(Socket socket, Context context, ArrayList<String> request, String ipAddr, int port) {
        this.socket = socket;
        this.context = context;
        this.request = request;
        this.ipAddr = ipAddr;
        this.port = port;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("start Senddatathread");

        if(request != null) {
            System.out.println("Req not null");
            try {
                if(socket == null) {
                    System.out.println("Socket null");
                    InetAddress inetAddress = Inet4Address.getByName(ipAddr);
                    SocketHandler.setSocket(new Socket(inetAddress, port));
                    System.out.println("Sock connected");
                }
                else if (!socket.isConnected()) {
                    System.out.println("Sock not connected");

                    InetAddress inetAddress = Inet4Address.getByName(ipAddr);
                    SocketHandler.setSocket(new Socket(inetAddress, port));
                    System.out.println("Sock connected");

                }
                socket = SocketHandler.getSocket();
                outputStream = socket.getOutputStream();
                System.out.println("Printing output");

                byte[] messageBytes = Utils.list2string(request).getBytes();
                outputStream.write(messageBytes);

                if(request.get(0).equals("login")) {
                    System.out.println("is login");
                    LocalBroadcastManager.getInstance(this.context).registerReceiver(NetworkService.getBroadcastReceiver(), new IntentFilter(Constants.START_RECEIVE));
                    Intent intent = new Intent(Constants.START_RECEIVE);
//                    intent.putExtra(Constants.LOGIN_DETAILS, request);
                    this.context.sendBroadcast(intent);
                    System.out.println("broadcast sent");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
