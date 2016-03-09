package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by MJ Alba on 2016-03-08.
 */
public class CurrentBidsActivity extends AppCompatActivity {

    private Profile profile; // the current user's profile
    private ListView currentBidsList; // view to display the current bids
    private CurrentBidsAdapter adapter; // current bids adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_bids);

        //adapter = new CurrentBidsAdapter(this, profile.getCurrentBids());
        //currentBidsList.setAdapter(adapter);

        profile = new Profile("name", "phone", "email");
        currentBidsList = (ListView) findViewById(R.id.currentBidsList);
    }
}
