package com.example.harshith.chatsockets;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
                    StringBuilder stringBuilder = new StringBuilder();

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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
