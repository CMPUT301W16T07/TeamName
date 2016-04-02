package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * The main controller for TutorTrader. Contains important
 * functions that need to be used by all other activities.
 * Also initializes the main data structures of the app.
 */

public class MethodsController extends AppCompatActivity {
    protected static final String SESSIONSFILE = "sessions.sav";
    protected static final String USERFILE = "profile.sav";
    protected static final String BIDFILE = "bids.sav";

    //TODO:load user profile if it exists or make new one.
    protected Profile currentProfile;

    protected ArrayList<Session> sessions = new ArrayList<Session>();
    protected ArrayList<Session> availableSessions = new ArrayList<Session>();
    protected ArrayList<Profile> profiles = new ArrayList<Profile>();
    protected ArrayList<Bid> bids = new ArrayList<Bid>();
    protected ArrayList<Session> upcomingSessions = new ArrayList<>();

    protected Button btn_availableSession, btn_myProfile, btn_CurrentBids, btn_mySessions;
    // sessionsofInterest holds the sessions belonging to the currentProfile
    protected ArrayList<Session> sessionsOfInterest = new ArrayList<Session>();

    private static final MethodsController instance = new MethodsController();

    static final int REQUEST_IMAGE_CAPTURE = 1234;
    protected Bitmap thumbnail;
    protected ImageView newImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load current profile
        //ArrayList<Profile> templist = new ArrayList<Profile>();
        loadProfile(USERFILE);

        if(profiles.size() == 0) {
            Profile tempProfile = new Profile("Default","Default","Default");
            profiles.add(tempProfile);
            //ArrayList<Profile> profiles = new ArrayList<Profile>();

            //Intent intent = new Intent(MethodsController.this, CreateProfileActivity.class);
           // startActivity(intent);


        }
        currentProfile = profiles.get(0);
        //loadSessions(SESSIONSFILE);
        loadElasticSearch();
    }

    protected MethodsController(){
        /*loadProfile(USERFILE);

        //loadSessions(SESSIONSFILE);
        if(currentProfile == null) {
            //ArrayList<Profile> profiles = new ArrayList<Profile>();
            Intent intent = new Intent(MethodsController.this, CreateProfileActivity.class);
            startActivity(intent);
            //currentProfile = new Profile("test username","test phone","test email");
            //profiles.add(currentProfile);
            //saveProfile(currentProfile);
        }*/
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
                finish();
            } else if (v.getId() == R.id.availableSessions) {
                Intent intent = new Intent(MethodsController.this, AvailableSessionsActivity.class);
                startActivity(intent);
                finish();
            } else if (v.getId() == R.id.currentBids) {
                Intent intent = new Intent(MethodsController.this, CurrentBidsActivity.class);
                startActivity(intent);
                finish();
            } else if (v.getId() == R.id.mySessions) {
                Intent intent = new Intent(MethodsController.this, MySessionsActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    /**
     * saveinFile borrowed from lonelyTwitter. Saves a list to a local file on the device.
     *
     * @param fileName specifies which file we are going to save to
     * @param list the arraylist we are saving to the file
     */
    public void saveInFile(String fileName, ArrayList list){
        try {
            FileOutputStream fos = openFileOutput(fileName,Context.MODE_PRIVATE);
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

    public void saveProfile(Profile profile){
        try {
            FileOutputStream fos = openFileOutput(USERFILE,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            //Type profileType = new TypeToken<Profile>() {
           // }.getType();
            Gson gson = new Gson();
            gson.toJson(profile, out);
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
            Type listType = new TypeToken<ArrayList<Profile>>() {}.getType();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-2016-19
            profiles = gson.fromJson(in, listType);

        }catch(FileNotFoundException e){
            //TODO Auto-generated catch block
            //currentProfile = new Profile("test username","test phone","test email");
            //saveProfile(currentProfile);
            profiles = new ArrayList<Profile>();
        }
    }

    /**
     * loadSessions in MySessions must load the sessions that are directly owned by the
     * current User. To do this, we index the sessions array list, matching only the sessions
     * that are owned by the user of interest.
     *
     * @param filename the name of the file containing all the sessions.
     */
    public void loadSessions (String filename) {

        //ArrayList<Session> allSessions;
        //allSessions = new ArrayList<>();
        sessionsOfInterest = new ArrayList<Session>();
        availableSessions = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-2016-19
            Type listType = new TypeToken<ArrayList<Session>>() {}.getType();
            sessions = gson.fromJson(in, listType);
            int size = sessions.size();
            UUID currentProfileID = currentProfile.getProfileID();
            for (int i = 0; i < size; i++){
                //TODO: we need to properly save and load profiles so the proper ProfileID is saved and not randomly generated each time we use the app
                UUID tutorProfileID = sessions.get(i).tutor.getProfileID();
                if (currentProfileID.compareTo(tutorProfileID) == 0) {
                    sessionsOfInterest.add(sessions.get(i));

                }
                if (sessions.get(i).getStatus().equals("available")) {
                    availableSessions.add(sessions.get(i));

                }
            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            sessionsOfInterest = new ArrayList<Session>();

        }
    }

    /**
     *
     */
    public void loadElasticSearch () {

        sessionsOfInterest = new ArrayList<Session>();
        availableSessions = new ArrayList<>();
        ElasticSessionController.GetSessionsTask getSessionsTask = new ElasticSessionController.GetSessionsTask();
        getSessionsTask.execute("");
        try {
            sessions = getSessionsTask.get();
            int size = sessions.size();
            UUID currentProfileID = currentProfile.getProfileID();
            for (int i = 0; i < size; i++){
                //TODO: we need to properly save and load profiles so the proper ProfileID is saved and not randomly generated each time we use the app
                UUID tutorProfileID = sessions.get(i).tutor.getProfileID();
                if (currentProfileID.compareTo(tutorProfileID) == 0) {
                    sessionsOfInterest.add(sessions.get(i));

                }
                if (sessions.get(i).getStatus().equals("available")) {
                    availableSessions.add(sessions.get(i));

                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * loadCurrentBids loads all bids from the sessions array that were made
     * by the current user.
     */
    public void loadCurrentBids () {

        int size = sessions.size(); // size of sessions
        UUID currentProfileID = currentProfile.getProfileID(); // current profileID

        // cycle through sessions
        for (int i = 0; i < size; i++){
            ArrayList<Bid> sessionBids = sessions.get(i).getBids();
            int sizeBids = sessionBids.size(); // size of that bids array

            // cycle through those bids
            for (int j = 0; j < sizeBids; j++) {

                // compare bidder with current profileID
                if (currentProfileID.compareTo(sessionBids.get(j).getBidder()) == 0) {

                    // and add to bids array if it's a match
                    bids.add(sessionBids.get(j));
                }
            }
        }
    }

    /**
     * Checks if the user is logged in. An alert give them the option to go back to avalible sessions
     * or Log in, calling the MyProfile to create a profile
     */
    public void verifyLogin(){

        if (currentProfile.isDefaultUser()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MethodsController.this);
            builder.setMessage("You must log in")
                    //.setCancelable(false)
                    .setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // fire an intent go to your next activity
                            //TODO: implement the delete process
                            Intent intent = new Intent(MethodsController.this, AvailableSessionsActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Create Profile", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(MethodsController.this, MyProfileActivity.class);
                            startActivity(intent);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

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

    /**
     * updateElasticSearch will update a given session if information was added to it.
     * It does this by removing the old session and adding the new one.
     * @param session session object we wish to update
     */
    protected void updateElasticSearch(Session session){
        // Remove old session that has information missing
        ElasticSessionController.RemoveSessionTask removeSessionTask = new ElasticSessionController.RemoveSessionTask();
        removeSessionTask.execute(session.getSessionID());

        //add new session that has the information we want to add
        ElasticSessionController.AddSessionTask addSessionTask = new ElasticSessionController.AddSessionTask();
        addSessionTask.execute(session);
        loadElasticSearch(); // load the newest addition
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

    public void Notify() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("Tutor Trader")
                .setContentText("You have a new notification");

        Intent resultIntent = new Intent(this, AvailableSessionsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AvailableSessionsActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            Bundle extras = data .getExtras();
            thumbnail = (Bitmap)extras.get("data");
            newImage.setImageBitmap(thumbnail);
            //saveInFile(SESSIONSFILE, sessions); //might not need this here

        }
    }

}
