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
 */
public class CurrentBidsActivity extends MethodsController {

    private ListView currentBidsList; // view to display the current bids
    private ArrayAdapter<Bid> adapter; // current bids adapter

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
        loadSessions(SESSIONSFILE);
        loadCurrentBids(); // reload the global bids array
        adapter = new ArrayAdapter<>(this,
                R.layout.current_bids_list_item, bids);
        currentBidsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
