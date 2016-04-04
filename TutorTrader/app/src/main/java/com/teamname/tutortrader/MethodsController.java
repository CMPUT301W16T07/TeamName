package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
    //protected static final String SESSIONSFILE = "sessions.sav";
    protected static final String USERFILE = "profile.sav";

    protected static final String OFFLINEFILE = "tempSession.sav";
    protected static final String BIDFILE = "bids.sav";



    //TODO:load user profile if it exists or make new one.
    protected Profile currentProfile;

    protected ArrayList<Session> sessions = new ArrayList<Session>();
    protected ArrayList<Session> availableSessions = new ArrayList<Session>();
    protected ArrayList<Profile> profiles = new ArrayList<Profile>();
    protected ArrayList<Profile> allProfiles = new ArrayList<>(); // similar to sessions array but for profiles
    protected ArrayList<Bid> bids = new ArrayList<Bid>();
    protected ArrayList<Session> upcomingSessions = new ArrayList<>();

    protected Button btn_availableSession, btn_myProfile, btn_CurrentBids, btn_mySessions;
    // sessionsofInterest holds the sessions belonging to the currentProfile
    protected ArrayList<Session> sessionsOfInterest = new ArrayList<Session>();

    private static final MethodsController instance = new MethodsController();
    static final int REQUEST_LOCATION = 9999;
    static final int REQUEST_IMAGE_CAPTURE = 1234;
    protected Bitmap thumbnail;
    protected ImageView newImage;
    static protected Boolean Connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setConnectivity();
        loadProfile(USERFILE);


        if(profiles.size() == 0) {
            Profile tempProfile = new Profile("Default","Default","Default");
            profiles.add(tempProfile);
        }
        currentProfile = profiles.get(0);
        loadElasticSearch();


    }

    protected MethodsController(){}


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

                Intent i = new Intent(MethodsController.this, AvailableSessionsActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

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
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(list, out);
            out.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void saveProfile(Profile profile){
        try {
            FileOutputStream fos = openFileOutput(USERFILE,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(profile, out);
            out.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public ArrayList<Session> loadOffline(){
        ArrayList<Session> temp = new ArrayList<Session>();
        try{

            FileInputStream fis = openFileInput(OFFLINEFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-2016-19
            Type listType = new TypeToken<ArrayList<Session>>() {}.getType();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-2016-19
            temp = gson.fromJson(in, listType);
            return temp;

        }catch(FileNotFoundException e){
            return temp;
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
            profiles = new ArrayList<Profile>();
        }
    }

    /**
     * Loads all sessions from Elastic Search database into proper arrays.
     */
    public void loadElasticSearch () {

        if(Connectivity) {
            sessionsOfInterest = new ArrayList<Session>();
            availableSessions = new ArrayList<>();

            ElasticSearchController.GetProfileTask getProfileTask = new ElasticSearchController.GetProfileTask();
            getProfileTask.execute("");
            try {
                allProfiles = getProfileTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //update sessions array
            ElasticSearchController.GetSessionsTask getSessionsTask = new ElasticSearchController.GetSessionsTask();
            getSessionsTask.execute("");
            try {
                sessions = getSessionsTask.get();
                int size = sessions.size();
                UUID currentProfileID = currentProfile.getProfileID();
                for (int i = 0; i < size; i++) {
                    UUID tutorProfileID = sessions.get(i).getTutorID();
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


            // code to notify peeps of the bids
            for (int i = 0; i < allProfiles.size(); i++) {
                if (allProfiles.get(i).getProfileID().equals(currentProfile.getProfileID())) {
                    if (allProfiles.get(i).isNewBid() == true) {
                        Notify();
                        allProfiles.get(i).setNewBid(false);
                        ElasticSearchController.UpdateProfileTask updateProfileTask = new ElasticSearchController.UpdateProfileTask();
                        updateProfileTask.execute(currentProfile);
                    }
                }
            }
        }

        //end of notify

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
     * Checks if the user is logged in. An alert give them the option to go back to available sessions
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

    /**
     * updateElasticSearchSession will update a given session if information was added to it.
     * It does this by removing the old session and adding the new one.
     * @param session session object we wish to update
     */
    protected void updateElasticSearchSession(Session session){

        ElasticSearchController.UpdateSessionTask updateSessionTask = new ElasticSearchController.UpdateSessionTask();
        updateSessionTask.execute(session);
       loadElasticSearch(); // load the newest addition
    }

    /**
     * updateElasticSearchProfile will update a given profile if information was added to it.
     * It does this by removing the old profile and adding the new one.
     * @param profile profile object we wish to update
     */
    protected void updateElasticSearchProfile(Profile profile){

        ElasticSearchController.UpdateProfileTask updateProfileTask = new ElasticSearchController.UpdateProfileTask();
        updateProfileTask.execute(profile);
        loadElasticSearch(); // load the newest addition

        //make sure current profile proper
        UUID currentUUID = currentProfile.getProfileID();
        Profile newCurrent = getProfile(currentUUID);
        currentProfile = newCurrent;
    }

    public void setCurrentProfile(Profile user){
        this.currentProfile = user;
    }

    public void Notify() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_notification)
                .setAutoCancel(true)
                .setContentTitle("Tutor Trader")
                .setContentText("Somebody has bid on one of your sessions!");

        Intent resultIntent = new Intent(this, MySessionsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MySessionsActivity.class);
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
        }
    }

    /**
     * get profile will return a profile object from elastic search if you pass in a profile UUID
     * @param uuid the profile's UUID
     * @return the Profile object
     */
    public static Profile getProfile (UUID uuid) {

        ArrayList <Profile> returnedProfile = new ArrayList<>();
        ElasticSearchController.GetProfileTask getProfileTask = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute("ProfileID", uuid.toString());
        try {
            returnedProfile = getProfileTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (returnedProfile.size() == 1) {
            return returnedProfile.get(0);
        } else {
            return null;
        }
    }

    /**
     * profileExists will return true if a username exists in the database, false otherwise
     * @param username the username to check.
     * @param userProfile the current user's Profile name (since we know that exists already)
     * @return Boolean.
     */
    public static Boolean profileExists (String username, String userProfile) {

        ArrayList <Profile> returnedProfile = new ArrayList<>();
        ElasticSearchController.GetProfileTask getProfileTask = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute("name", username);

        // search the database
        try {
            returnedProfile = getProfileTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // if we found a match
        if (returnedProfile.size() == 1) {

            // make sure it isn't the current profile
            return (!returnedProfile.get(0).getName().equals(userProfile));

        // if we didn't find a match, profile doesn't exist
        } else {
            return false;
        }
    }

    /**
     * get session will return a session object from elastic search if you pass in a session UUID
     * @param uuid the session's UUID
     * @return the Session object
     */
    static public Session getSession (UUID uuid) {
        ArrayList <Session> returnedSession = new ArrayList<>();
        ElasticSearchController.GetSessionsTask getSessionsTask = new ElasticSearchController.GetSessionsTask();
        getSessionsTask.execute("sessionID", uuid.toString());
        try {
            returnedSession = getSessionsTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (returnedSession.size() == 1) {
            return returnedSession.get(0);
        } else {
            return null;
        }
    }

    /**
     * http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
     * retruns true if connected to internet
     * @return
     */
    public void checkConnectivity(){

        setConnectivity();
        if(!Connectivity){

                Toast.makeText(MethodsController.this, "No Internet! \n continuing in offline mode", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MethodsController.this, MySessionsActivity.class);
                startActivity(intent);
        }
    }

    public void setConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Connectivity = Boolean.TRUE;
            //Upload offline save file
            ArrayList<Session> toUpload = loadOffline();
            if(toUpload.size() > 0 ) {
                for (int i = 0; i < toUpload.size(); i++) {
                    Session session = toUpload.get(i);
                    ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
                    addSessionTask.execute(session);
                }
                toUpload.clear();
                saveInFile(OFFLINEFILE,toUpload);
            }
        } else {
            Connectivity = Boolean.FALSE;
        }
    }
}
