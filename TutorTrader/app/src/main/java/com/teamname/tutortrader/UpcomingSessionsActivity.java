package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * UpcomingSessionsActivity displays sessions that the user has bid on and
 * has been accepted by the tutor. Clicking on a list item will launch an activity
 * which gives you options to handle an upcoming session.
 *
 */
public class UpcomingSessionsActivity extends MethodsController {

    private CurrentBidsAdapter adapter; // current bids adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_sessions);
        ListView upcomingSessionsList = (ListView) findViewById(R.id.upcomingSessionsList);
        upcomingSessionsList.setBackgroundResource(R.drawable.apple_righ);

        loadSessions(SESSIONSFILE);
        loadCurrentBids();
        // TODO: instead of displaying bids we need to display the session that the bid is for.
        ArrayList<Bid> acceptedBids = new ArrayList<>();
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

        //TODO: this will launch an activity that displays the session (Very similar to ViewOneSession)
        upcomingSessionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: make a mock ViewOneSessionActivity that will give options to show close, reopen and rate
                Intent intent = new Intent(UpcomingSessionsActivity.this, ViewOneSessionActivity.class);
                String index = String.valueOf(position);
                // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });

    }

}
