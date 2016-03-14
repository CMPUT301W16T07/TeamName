package com.teamname.tutortrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AvailableSessionsActivity extends MethodsController {


    private ListView oldSessions;
   // private ArrayList<Session> sessions = new ArrayList<Session>();
    private ArrayAdapter<Session> adapter;
    protected EditText query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_sessions);
        btn_CurrentBids = (Button) findViewById(R.id.currentBids);
        btn_CurrentBids.setOnClickListener(btnClickListener);
        btn_myProfile = (Button) findViewById(R.id.myProfile);
        btn_myProfile.setOnClickListener(btnClickListener);
        btn_mySessions = (Button) findViewById(R.id.mySessions);
        btn_mySessions.setOnClickListener(btnClickListener);
        btn_availableSession = (Button) findViewById(R.id.availibleSessions);
        btn_availableSession.setOnClickListener(btnClickListener);


        //populates the list of all sessions
        oldSessions = (ListView) findViewById(R.id.sessionList);
        loadSessions(SESSIONSFILE);
        adapter = new ArrayAdapter<>(this,
                R.layout.session_list_item, sessions);
        oldSessions.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        // TODO implement seaching once elastic search is working
        Button searchbutton = (Button) findViewById(R.id.searchButton);
        query = (EditText) findViewById(R.id.searchtext);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setResult(RESULT_OK);
                    String searchstring = query.getText().toString();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutor_trade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile(SESSIONSFILE);
        //adapter = new ArrayAdapter<Session>(this, R.layout.session_list_item);
        oldSessions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //TODO: load list to contorller



    }


    /**
     * loadFromFile in Availible must load all session.
     *
     * @param filename the name of the file containing all the sessions.
     */
    private void loadFromFile (String filename) {

        try {
            FileInputStream fis = openFileInput(SESSIONSFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-2016-19
            Type listType = new TypeToken<ArrayList<Session>>() {
            }.getType();
            sessions = gson.fromJson(in, listType);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

        }
    }



}
