package com.example.harshith.chatsockets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String username = getIntent().getStringExtra(Constants.USERNAME);


    }
    
}
