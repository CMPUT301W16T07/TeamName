package com.teamname.tutortrader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    protected Profile currentProfile;

    protected ArrayList<Session> sessions = new ArrayList<Session>();
    protected ArrayList<Profile> profiles = new ArrayList<Profile>();
    protected ArrayList<Bid> bids = new ArrayList<Bid>();

    protected Button btn_availableSession, btn_myProfile, btn_CurrentBids, btn_mySessions;

    private static final MethodsController instance = new MethodsController();

    private void MethodsController(){
        //Load current profile
        loadProfile(USERFILE);
        if(currentProfile.getName() == null) {
            //TODO: make new profile
        }
    }
    /**
     * This method is used so whenever a person clicks one of the buttons at the top of the screen
     * we can switch to that screen
     *
     * @param view this is saved on the button listener when you click the button
     */
    protected View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.myProfile) {
                Intent intent = new Intent(MethodsController.this, MyProfileActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.availibleSessions) {
                Intent intent = new Intent(MethodsController.this, AvailableSessionsActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.currentBids) {
                Intent intent = new Intent(MethodsController.this, CurrentBidsActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.mySessions) {
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

    private void loadProfile(String filename){
        try{
            FileInputStream fis = openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-2016-19
            Type profileType = new TypeToken<Profile>() {
            }.getType();
            currentProfile = gson.fromJson(in, profileType);

        }catch(FileNotFoundException e){
            //TODO Auto-generated catch block
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

    public static MethodsController getInstance(){
        return instance;
    }

    public Profile getCurrentProfile(){
        return currentProfile;
    }

    public void setCurrentProfile(Profile user){
        this.currentProfile = user;
    }

}
