package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by MJ Alba on 2016-03-08.
 *
 * The activity that allows users to view a list of all
 * the sessions they've created.
 */
public class MySessionsActivity extends MethodsController {
    //final MethodsController instance = MethodsController.getInstance();
    //final Profile currentProfile = instance.getCurrentProfile();

    private ListView oldSessionsList;

    // inspired by lonelyTwitter code
    private ArrayAdapter<Session> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sessions);
        oldSessionsList = (ListView) findViewById(R.id.sessionList);
        oldSessionsList.setBackgroundResource(R.drawable.apple_left);
        btn_CurrentBids = (Button) findViewById(R.id.currentBids);
        btn_CurrentBids.setOnClickListener(btnClickListener);
        btn_myProfile = (Button) findViewById(R.id.myProfile);
        btn_myProfile.setOnClickListener(btnClickListener);
        btn_mySessions = (Button) findViewById(R.id.mySessions);
        btn_mySessions.setOnClickListener(btnClickListener);
        btn_availableSession = (Button) findViewById(R.id.availibleSessions);
        btn_availableSession.setOnClickListener(btnClickListener);
        //load from file to fill screen with sessions pertaining to the user
        // load through file or through elastic search?

        verifyLogin();


        loadSessions(SESSIONSFILE);
        adapter = new ArrayAdapter<>(this,
          R.layout.list_colour,sessionsOfInterest);
        oldSessionsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Adding a new session
        Button addNewSession = (Button) findViewById(R.id.addNewSession);
        addNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySessionsActivity.this, AddSessionActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(MySessionsActivity.this, ViewOneSessionActivity.class);
                String index = String.valueOf(position);
                // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });



    }

}
