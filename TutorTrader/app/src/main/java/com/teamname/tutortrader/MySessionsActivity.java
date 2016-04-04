package com.teamname.tutortrader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * The activity that allows users to view a list of all
 * the sessions they've created.
 */
public class MySessionsActivity extends MethodsController {

    private ArrayList<Session> offlineSessions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView oldSessionsList;
        // inspired by lonelyTwitter code
        ArrayAdapter<Session> adapter;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sessions);
        oldSessionsList = (ListView) findViewById(R.id.sessionList);
        oldSessionsList.setBackgroundResource(R.drawable.apple_righ);
        btn_CurrentBids = (Button) findViewById(R.id.currentBids);
        btn_CurrentBids.setOnClickListener(btnClickListener);
        btn_myProfile = (Button) findViewById(R.id.myProfile);
        btn_myProfile.setOnClickListener(btnClickListener);
        btn_mySessions = (Button) findViewById(R.id.mySessions);
        btn_mySessions.setOnClickListener(btnClickListener);
        btn_availableSession = (Button) findViewById(R.id.availableSessions);
        btn_availableSession.setOnClickListener(btnClickListener);

        // verify user is logged in
        verifyLogin();
        setConnectivity();

        // set activity title
        TextView activityTitle = (TextView) findViewById(R.id.activityTitle);
        activityTitle.setText(R.string.MySessionsButton);

        loadElasticSearch();
        if (Connectivity) {
            adapter = new MySessionsAdapter(this, sessionsOfInterest);

        } else {
            offlineSessions = loadOffline();
            adapter = new MySessionsAdapter(this, offlineSessions);

        }
        oldSessionsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Adding a new session
        Button addNewSession = (Button) findViewById(R.id.addNewSession);
        addNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentProfile.isDefaultUser()) {

                    Intent intent = new Intent(MySessionsActivity.this, AddSessionActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MySessionsActivity.this, "You need to have a profile to use this feature.\n Please connect to internet to continue", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Selecting a session from list
        // TODO: pass the index of the list item to fill fields in the ViewOneSession activity
        /*
            http://stackoverflow.com/questions/2468100/android-listview-click-howto
            If user clicks on an entry in the listview, the index of the entry clicked is
            passed onto the EditEntry activity, so editing can be done on the correct entry.
         */
        oldSessionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Connectivity) {
                    Intent intent = new Intent(MySessionsActivity.this, ViewOneSessionActivity.class);

                    // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                    intent.putExtra("index", position);
                    startActivity(intent);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Must Connect to Internet!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }
}
