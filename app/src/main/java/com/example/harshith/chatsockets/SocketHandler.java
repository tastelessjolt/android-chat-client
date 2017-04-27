package com.example.harshith.chatsockets;

import java.net.Socket;

/**
 * Created by harshith on 4/27/17.
 */

public class SocketHandler {
    private static Socket socket = null;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}
