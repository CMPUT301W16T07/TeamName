package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewBidsActivity extends MethodsController {
    private ListView allBidsList;
    private ArrayAdapter<Bid> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);
        Intent intent = getIntent();
        /*
            The index of the entry that was clicked in the list of entries displayed on the main
            screen is passed through the intent. Here is where we access it
        */
        final String index_receive = intent.getStringExtra("index");
        final int index_r = Integer.parseInt(index_receive);
        // populates the list of all bids
        allBidsList = (ListView) findViewById(R.id.bidsListView);
        loadSessions(SESSIONSFILE);
        adapter = new CurrentBidsAdapter(this, sessionsOfInterest.get(index_r).getBids());
        //adapter = new ArrayAdapter<>(this,
        //        R.layout.list_colour, sessionsOfInterest.get(index_r).getBids());
        allBidsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        final Button sessionButton = (Button) findViewById(R.id.sessionButton);
        sessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBidsActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        allBidsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewBidsActivity.this);
                final Integer positionCopy = position;
                final Bid currentBid = sessionsOfInterest.get(index_r).getBids().get(position);
                if (currentBid.getStatus().equals("accepted")) {
                    AlertDialog.Builder builderNO = new AlertDialog.Builder(ViewBidsActivity.this);
                    builderNO.setMessage("Session already accepted!")
                            .setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builderNO.create();
                    alert.show();
                }  else {
                    builder.setMessage("Accept Bid for $" + currentBid.getAmount() + "?")
                            .setCancelable(false)
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Integer index = sessions.indexOf(sessionsOfInterest.get(index_r));
                                    sessions.get(index).setStatus("booked");
                                    sessions.get(index).getBids().get(positionCopy).setStatus("accepted");
                                    sessions.get(index).getBids().clear();
                                    sessions.get(index).getBids().add(currentBid);
                                    
                                    //TODO: notify bidder on acceptance
                                    saveInFile(SESSIONSFILE, sessions);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                 }
            }
        });


    }
}
