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

    private Button btn_availableSession, btn_myProfile, btn_CurrentBids, btn_mySessions;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        btn_CurrentBids = (Button) findViewById(R.id.currentBids);
        btn_CurrentBids.setOnClickListener(onClickListener);
        btn_myProfile = (Button) findViewById(R.id.myProfile);
        btn_myProfile.setOnClickListener(onClickListener);
        btn_mySessions = (Button) findViewById(R.id.mySessions);
        btn_mySessions.setOnClickListener(onClickListener);
        btn_availableSession = (Button) findViewById(R.id.availibleSessions);
        btn_availableSession.setOnClickListener(onClickListener);
    }


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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btn_myProfile) {
                setContentView(R.layout.my_profile);
                Intent intent = new Intent(MethodsController.this, MyProfileActivity.class);
                startActivity(intent);
            } else if (v == btn_availableSession) {
                setContentView(R.layout.available_sessions);
                Intent intent = new Intent(MethodsController.this, AvailableSessionsActivity.class);
                startActivity(intent);
            } else if (v == btn_CurrentBids) {
                setContentView(R.layout.current_bids);
                Intent intent = new Intent(MethodsController.this, CurrentBidsActivity.class);
                startActivity(intent);
            } else if (v == btn_mySessions) {
                setContentView(R.layout.my_sessions);
                Intent intent = new Intent(MethodsController.this, MySessionsActivity.class);
                startActivity(intent);
            }
        }
    };
}
