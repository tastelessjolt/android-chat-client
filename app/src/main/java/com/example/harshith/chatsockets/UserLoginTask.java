package com.example.harshith.chatsockets;

/**
 * Created by harshith on 4/27/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    final String mEmail;
    final String mPassword;
    private final String mIpAddr;
    private final int mPort;

    ArrayList<String> vector;


    UserLoginTask(String email, String password, String ip_addr, int port) {
        mEmail = email;
        mPassword = password;
        mIpAddr = ip_addr;
        mPort = port;
        vector = new ArrayList<String>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            // Simulate network access.

            InetAddress inetAddress = Inet4Address.getByName(mIpAddr);
            Socket socket = new Socket();
            SocketHandler.setSocket(socket);
            SocketHandler.getSocket().connect(new InetSocketAddress(inetAddress, mPort));
            InputStream inputStream = SocketHandler.getSocket().getInputStream();
            OutputStream outputStream = SocketHandler.getSocket().getOutputStream();

            ArrayList<String> messageVector = new ArrayList<String>() {
                {
                    add("login");
                    add(mEmail);
                    add(mPassword);
                }
            };

            byte[] message = (Utils.list2string(messageVector)).getBytes();
            outputStream.write(message);

            byte[] buffer = new byte[256];
            int endPos = 0;
            int bytes = inputStream.read(buffer);
            String data = new String(buffer).substring(0, bytes);
            Log.d("stream", data);
            StringBuilder stringBuilder = new StringBuilder();
            while((endPos = data.indexOf('#')) < 0) {
                stringBuilder.append(data);
                bytes = inputStream.read(buffer);
                if (bytes == -1) {
                    return false;
                } else {
                    data = new String(buffer).substring(0, bytes);
                    Log.d("stream", data);
                }
            }
            Log.d("Meg", stringBuilder.toString());
            Log.d("End Pos", endPos + "");
            stringBuilder.append(data.substring(0, endPos + 1));

            vector = Utils.string2list(stringBuilder.toString());
        }  catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//            Log.d("Mag", vector.toString() );



        // TODO: register the new account here.
        return true;
    }


}
