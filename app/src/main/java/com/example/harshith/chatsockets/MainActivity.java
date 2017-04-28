package com.example.harshith.chatsockets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Map<String, ?> userData;
    String left_over;

    ArrayList<Person> userList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = new ArrayList<Person>();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
                        if (login.size() > 0) {
                            Gson gson = new Gson();
                            final String allUsers = gson.toJson(Person.getPersonArray(all_users));
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.LOGIN_FILE_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.LOGGED_IN, true);
                            editor.putString(Constants.USERNAME, mEmail);
                            editor.putString(Constants.PASSWORD, mPassword);
                            editor.putString(Constants.NAME, login.get(2));
                            editor.putString(Constants.ALL_USERS, allUsers);
                            editor.commit();

                            userList.addAll(Person.getPersonArray(all_users));

                            mAdapter.notifyDataSetChanged();

                            left_over = data;

                            textView.setText(login.get(2));
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
    protected void onStart() {
        super.onStart();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)

        mAdapter = new MyAdapter(userList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Person person = userList.get(position);
                Toast.makeText(getApplicationContext(), person.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        String DEBUG_TAG = "gestures";
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(DEBUG_TAG,"Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(DEBUG_TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(DEBUG_TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(DEBUG_TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
