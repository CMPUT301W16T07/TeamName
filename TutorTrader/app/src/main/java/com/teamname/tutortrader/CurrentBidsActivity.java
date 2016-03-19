package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.MemoryHandler;

/**
 * Created by MJ Alba on 2016-03-08.
 *
 * The activity that shows a list of a current users bids
 * on OTHER users' sessions.
 */
public class CurrentBidsActivity extends MethodsController {

    private ListView currentBidsList; // view to display the current bids
    private CurrentBidsAdapter adapter; // current bids adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_bids);
        btn_CurrentBids = (Button) findViewById(R.id.currentBids);
        btn_CurrentBids.setOnClickListener(btnClickListener);
        btn_myProfile = (Button) findViewById(R.id.myProfile);
        btn_myProfile.setOnClickListener(btnClickListener);
        btn_mySessions = (Button) findViewById(R.id.mySessions);
        btn_mySessions.setOnClickListener(btnClickListener);
        btn_availableSession = (Button) findViewById(R.id.availibleSessions);
        btn_availableSession.setOnClickListener(btnClickListener);

        // populates the list of all bids
        currentBidsList = (ListView) findViewById(R.id.currentBidsList);
        currentBidsList.setBackgroundResource(R.drawable.apple_righ);
        loadSessions(SESSIONSFILE);
        loadCurrentBids(); // reload the global bids array
        adapter = new CurrentBidsAdapter(this, bids);
        currentBidsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button clearDeclinedBidsButton = (Button) findViewById(R.id.clearDeclinedBidsButton);
        clearDeclinedBidsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCurrentBids();
                // cycle through those bids
                for (int i = 0; i < bids.size(); i++) {
                    // weed out declined bids
                    if (bids.get(i).getStatus().equals("declined")) {
                        bids.remove(i);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}
