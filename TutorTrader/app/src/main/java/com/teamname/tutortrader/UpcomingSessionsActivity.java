package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * UpcomingSessionsActivity displays sessions that the user has bid on and
 * has been accepted by the tutor. Clicking on a list item will launch an activity
 * which gives you options to handle an upcoming session.
 *
 */
public class UpcomingSessionsActivity extends MethodsController {
    private ArrayList<Bid> acceptedBids = new ArrayList<>();
    private CurrentBidsAdapter adapter; // current bids adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_sessions);
        ListView upcomingSessionsList = (ListView) findViewById(R.id.upcomingSessionsList);
        upcomingSessionsList.setBackgroundResource(R.drawable.apple_righ);

        //loadSessions(SESSIONSFILE);
        loadElasticSearch();
        loadCurrentBids();
        // TODO: instead of displaying bids we need to display the session that the bid is for.
        acceptedBids = new ArrayList<>();
        for (int i =0; i<bids.size(); i++) {
            if (bids.get(i).getStatus().equals("accepted")){
                //upcomingSessions.add(bids.get(i).getBidder())
                acceptedBids.add(bids.get(i));
            }
        }
        // populates the list of all bids
        adapter = new CurrentBidsAdapter(this, acceptedBids);
        upcomingSessionsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button upcomingBackButton = (Button) findViewById(R.id.upcomingBackButton);
        upcomingBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back an activity
                finish();
            }
        });

        upcomingSessionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UpcomingSessionsActivity.this, ViewOneUpcomingSession.class);
                Session clickedSession = getSession(acceptedBids.get(position).getSessionID());
                UUID clickedSessionID = clickedSession.getSessionID();
                int wantedIndex = -1;
                for (int i=0; i<sessions.size();i++) {
                    if (sessions.get(i).getSessionID().equals(clickedSessionID)) {
                        wantedIndex = i;
                    }
                }
                String index = String.valueOf(wantedIndex);
                // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });

    }

}
