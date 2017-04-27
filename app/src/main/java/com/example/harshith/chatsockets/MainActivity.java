package com.example.harshith.chatsockets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Map<String, ?> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.LOGIN_FILE_KEY, Context.MODE_PRIVATE);
        userData = sharedPreferences.getAll();

        final TextView textView = (TextView) findViewById(R.id.name);
//        textView.setText((String) userData.get(Constants.NAME));
        if(SocketHandler.getSocket() == null){
            UserLoginTask userLoginTask = new UserLoginTask((String) userData.get(Constants.USERNAME), (String) userData.get(Constants.PASSWORD),
                    (String) userData.get(Constants.IP_ADDR), (Integer) userData.get(Constants.PORT)) {
                @Override
                protected void onPostExecute(final Boolean success) {
                    //                mAuthTask = null;
                    //                showProgress(false);

                    if (success) {
                        if (vector.size() > 0) {
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.LOGIN_FILE_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.LOGGED_IN, true);
                            editor.putString(Constants.USERNAME, mEmail);
                            editor.putString(Constants.PASSWORD, mPassword);
                            editor.putString(Constants.NAME, vector.get(2));
                            editor.commit();

                            textView.setText(vector.get(2));
                        }
                    } else {

                    }
                }

                @Override
                protected void onCancelled() {
                    //                mAuthTask = null;
                    //                showProgress(false);
                }
            };

            userLoginTask.execute((Void) null);
        }
        else {
            textView.setText((String) userData.get(Constants.NAME));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Main", "Destroying");
        if(SocketHandler.getSocket() != null) {
            try {
                SocketHandler.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
