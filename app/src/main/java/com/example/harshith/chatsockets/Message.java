package com.example.harshith.chatsockets;

/**
 * Created by harshith on 4/30/17.
 */

public class Message {
    public String message;
    public boolean yours;

    public Message() {

    }
    public Message(String message, boolean yours) {
        this.message = message;
        this.yours = yours;
    }
}
