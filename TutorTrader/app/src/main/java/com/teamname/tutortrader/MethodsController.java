package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by iali1 on 3/8/16.
 */
public class MethodsController extends AppCompatActivity {
    private static final String SESSIONSFILE = "sessions.sav";
    private static final String USERFILE = "profile.sav";
    private static final String BIDFILE = "bids.sav";

    private ArrayList<Session> sessions = new ArrayList<Session>();
    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private ArrayList<Bid> bids = new ArrayList<Bid>();

    private void saveInFile(String fileName){
        //TODO: Implement this
    }

    private void loadFromFile(String fileName){
        //TODO: Implement this
    }

    private void verifyFields(){
        //TODO: not needed straight away so don't need to do this yet
    }

    private void updateStatus(){
        //TODO: Implement this
    }

    private Boolean updateDatabase(){
        //TODO: not needed straight away so don't do this yet
        return Boolean.FALSE;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_sessions);

        Button availableSessions = (Button) findViewById(R.id.availibleSessions);
        availableSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MethodsController.this, AvailableSessionsActivity.class);
                startActivity(intent);
            }
        });

        Button mySessions = (Button) findViewById(R.id.mySessions);
        mySessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MethodsController.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Clicking on the "Current Bids" tab.
         */
        Button currentBids = (Button) findViewById(R.id.currentBids);
        currentBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MethodsController.this, CurrentBidsActivity.class);
                startActivity(intent);
            }
        });

        Button myProfile = (Button) findViewById(R.id.myProfile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MethodsController.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

    }

}
