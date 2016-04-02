package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The activity that shows a list of a current users bids
 * on OTHER users' sessions.
 */
public class CurrentBidsActivity extends MethodsController {


    private CurrentBidsAdapter adapter; // current bids adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView currentBidsList; // view to display the current bids

        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_bids);
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
        activityTitle.setText(R.string.CurrentBidsButton);

        // populates the list of all bids
        currentBidsList = (ListView) findViewById(R.id.currentBidsList);
        currentBidsList.setBackgroundResource(R.drawable.apple_righ);
        //loadSessions(SESSIONSFILE);

        loadCurrentBids(); // reload the global bids array
        adapter = new CurrentBidsAdapter(this, bids);
        currentBidsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button upcomingSessionsButton = (Button) findViewById(R.id.upcomingSessionsButton);
        upcomingSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCurrentBids();
                Intent intent = new Intent(CurrentBidsActivity.this, UpcomingSessionsActivity.class);
                startActivity(intent);
                // TODO: make it show all the bids that have been accepted

            }
        });
    }

}
