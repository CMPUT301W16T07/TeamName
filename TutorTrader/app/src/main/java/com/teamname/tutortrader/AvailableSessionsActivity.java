package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 *
 * The activity for the list of all available sessions (the main
 * screen of the app).
 */
public class AvailableSessionsActivity extends MethodsController {


    private ListView oldSessions;
   // private ArrayList<Session> sessions = new ArrayList<Session>();
    private ArrayAdapter adapter;
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
        btn_availableSession = (Button) findViewById(R.id.availableSessions);
        btn_availableSession.setOnClickListener(btnClickListener);

        // set activity title
        TextView activityTitle = (TextView) findViewById(R.id.activityTitle);
        activityTitle.setText(R.string.AvailableSessionsButton);

        //populates the list of all sessions
        oldSessions = (ListView) findViewById(R.id.sessionList);
        oldSessions.setBackgroundResource(R.drawable.apple_righ);
        /*ElasticSessionController.GetSessionsTask getSessionsTask = new ElasticSessionController.GetSessionsTask();
        getSessionsTask.execute("");
        try {
            availableSessions = getSessionsTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        loadElasticSearch();

        /**
         * Following work was redacted as elastic search made it obsolete
         */
        //loadSessions(SESSIONSFILE);
        //ElasticSessionController.getLatestSessions();
        //available sessions will only contain available sessions that are not booked
        /*ArrayList<Session> availableSessions = new ArrayList<>();
        for (int i=0;i<sessions.size();i++) {
            if (!sessions.get(i).getStatus().equals("booked")) {
                availableSessions.add(sessions.get(i));
            }
        }*/



        //adapter = new AvailableSessionsAdapter(this, sessions);
        adapter = new AvailableSessionsAdapter(this, availableSessions);
        oldSessions.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        adapter = new ArrayAdapter<>(this,
//                R.layout.list_colour, availableSessions);
//        oldSessions.setAdapter(adapter);
//        adapter.notifyDataSetChanged();


        /**
         * handles the search button press.
         * When button is pressed takes whatever text is entered in the search field and
         * querys elastic search for that text. Searches both titles and
         * descriptions. It also does not add sessions twice if the search
         * word is in the Title and description
         */
        Button searchbutton = (Button) findViewById(R.id.searchButton);
        query = (EditText) findViewById(R.id.searchtext);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                ArrayList<Session> searchedSessions = new ArrayList<Session>();
                ArrayList<Session> tempsearchedSessions = new ArrayList<Session>();
                String searchstring = query.getText().toString();
                if (searchstring.length() != 0) {  //Check if they are searching anything
                    ElasticSearchController.GetSessionsTask getSessionsTask = new ElasticSearchController.GetSessionsTask();
                    getSessionsTask.execute("title", searchstring);
                    try {
                        searchedSessions = getSessionsTask.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    ElasticSearchController.GetSessionsTask moreSessionsTask = new ElasticSearchController.GetSessionsTask();
                    moreSessionsTask.execute("description", searchstring);
                    try {
                        tempsearchedSessions = (moreSessionsTask.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (searchedSessions.size() == 0) {
                        searchedSessions.addAll(tempsearchedSessions);
                    }
                    for (int i = 0; i < tempsearchedSessions.size(); i++) {
                        boolean in = false;
                        for (int q = 0; q < searchedSessions.size(); q++) {
                            if (tempsearchedSessions.get(i) == searchedSessions.get(q)) {
                                in = true;
                            }
                            if (in = false) {
                                searchedSessions.add(tempsearchedSessions.get(i));
                            }
                        }
                    }
                    adapter = new AvailableSessionsAdapter(AvailableSessionsActivity.this, searchedSessions);
                    oldSessions.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    loadElasticSearch();
                    adapter = new AvailableSessionsAdapter(AvailableSessionsActivity.this, availableSessions);
                    oldSessions.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }

        });


        oldSessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AvailableSessionsActivity.this, BidOnSessionActivity.class);
                String index = String.valueOf(position);
                // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });

        checkConnectivity();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tutor_trade, menu);
//        return true;
//    }

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
        //ElasticSessionController.GetSessionsTask loadTask = new ElasticSessionController.GetSessionsTask();
        //loadTask.execute("");
        //adapter = new ArrayAdapter<Session>(this, R.layout.session_list_item);
        oldSessions = (ListView) findViewById(R.id.sessionList);

        //loadFromFile(SESSIONSFILE);
        loadElasticSearch();
        adapter = new AvailableSessionsAdapter(this, availableSessions);
        oldSessions.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }


    /**
     * loadFromFile in Availible must load all session.
     *
     * @param filename the name of the file containing all the sessions.
     *
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
    }*/



}
