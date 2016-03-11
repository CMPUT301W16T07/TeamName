package com.teamname.tutortrader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MySessionsActivity extends MethodsController {


    private ListView oldSessionsList;
    private ArrayList<Session> sessionsOfInterest = new ArrayList<Session>(); //this creates a list of sessions
    // inspired by lonelyTwitter code
    private ArrayAdapter<Session> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sessions);
        //load from file to fill screen with sessions pertaining to the user
        // load through file or through elastic search?

        loadFromFile(SESSIONSFILE);
        adapter = new ArrayAdapter<Session>(this,
                R.layout.my_sessions_list,sessionsOfInterest);
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
        Intent intent = new Intent(MySessionsActivity.this, ViewOneSessionActivity.class);


    }



    /**
     * loadFromFile in MySessions must load the sessions that are directly owned by the
     * current User. To do this, we index the sessions array list, matching only the sessions
     * that are owned by the user of interest.
     *
     * @param filename the name of the file containing all the sessions.
     */
    private void loadFromFile (String filename) {

        ArrayList<Session> allSessions = new ArrayList<Session>();
        sessionsOfInterest = new ArrayList<Session>();
        try {
            FileInputStream fis = openFileInput(SESSIONSFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-2016-19
            Type listType = new TypeToken<ArrayList<Session>>() {
            }.getType();
            allSessions = gson.fromJson(in, listType);
            for (int i =0; i < allSessions.size();i++){
                if (allSessions.get(i).tutor.getProfileID() == currentProfile.getProfileID()) {
                    sessionsOfInterest.add(allSessions.get(i));
                }
            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            sessionsOfInterest = new ArrayList<Session>();

        }
    }
}
