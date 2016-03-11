package com.teamname.tutortrader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by iali1 on 3/8/16.
 */
public class MethodsController extends AppCompatActivity {
    protected static final String SESSIONSFILE = "sessions.sav";
    protected static final String USERFILE = "profile.sav";
    protected static final String BIDFILE = "bids.sav";

    //TODO:load user profile if it exists or make new one.
    protected Profile currentProfile = Profile.getInstance();

    protected ArrayList<Session> sessions = new ArrayList<Session>();
    protected ArrayList<Profile> profiles = new ArrayList<Profile>();
    protected ArrayList<Bid> bids = new ArrayList<Bid>();

    protected Button btn_availableSession, btn_myProfile, btn_CurrentBids, btn_mySessions;


//http://stackoverflow.com/questions/7873480/android-one-onclick-method-for-multiple-buttons

    protected View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.myProfile) {
                Intent intent = new Intent(MethodsController.this, MyProfileActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.availibleSessions) {
                setContentView(R.layout.available_sessions);
                btn_CurrentBids = (Button) findViewById(R.id.currentBids);
                btn_CurrentBids.setOnClickListener(btnClickListener);
                btn_myProfile = (Button) findViewById(R.id.myProfile);
                btn_myProfile.setOnClickListener(btnClickListener);
                btn_mySessions = (Button) findViewById(R.id.mySessions);
                btn_mySessions.setOnClickListener(btnClickListener);
                btn_availableSession = (Button) findViewById(R.id.availibleSessions);
                btn_availableSession.setOnClickListener(btnClickListener);
                Intent intent = new Intent(MethodsController.this, AvailableSessionsActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.currentBids) {
                setContentView(R.layout.current_bids);
                btn_CurrentBids = (Button) findViewById(R.id.currentBids);
                btn_CurrentBids.setOnClickListener(btnClickListener);
                btn_myProfile = (Button) findViewById(R.id.myProfile);
                btn_myProfile.setOnClickListener(btnClickListener);
                btn_mySessions = (Button) findViewById(R.id.mySessions);
                btn_mySessions.setOnClickListener(btnClickListener);
                btn_availableSession = (Button) findViewById(R.id.availibleSessions);
                btn_availableSession.setOnClickListener(btnClickListener);
                Intent intent = new Intent(MethodsController.this, CurrentBidsActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.mySessions) {
                setContentView(R.layout.my_sessions);
                btn_CurrentBids = (Button) findViewById(R.id.currentBids);
                btn_CurrentBids.setOnClickListener(btnClickListener);
                btn_myProfile = (Button) findViewById(R.id.myProfile);
                btn_myProfile.setOnClickListener(btnClickListener);
                btn_mySessions = (Button) findViewById(R.id.mySessions);
                btn_mySessions.setOnClickListener(btnClickListener);
                btn_availableSession = (Button) findViewById(R.id.availibleSessions);
                btn_availableSession.setOnClickListener(btnClickListener);
                Intent intent = new Intent(MethodsController.this, MySessionsActivity.class);
                startActivity(intent);
            }
        }
    };

    /**
     * saveinFile borrowed from lonelyTwitter.
     *
     * @param fileName specifies which file we are going to save to
     * @param list the arraylist we are saving to the file
     */
    public void saveInFile(String fileName, ArrayList list){
        try {
            FileOutputStream fos = openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(list, out);
            out.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /*public void loadFromFile(String fileName){
        //TODO: Implement this
    }*/

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

}
