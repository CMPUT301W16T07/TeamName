package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ViewBidsActivity extends MethodsController {

    private ArrayAdapter<Bid> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView allBidsList;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);
        Intent intent = getIntent();

        /*
            The index of the entry that was clicked in the list of entries displayed on the main
            screen is passed through the intent. Here is where we access it
        */
        final int index_r = intent.getIntExtra("index", 0);

        // populates the list of all bids
        allBidsList = (ListView) findViewById(R.id.bidsListView);
        allBidsList.setBackgroundResource(R.drawable.black_chalkboard);
        loadElasticSearch();
        adapter = new CurrentBidsOnMySessionsAdapter(this, sessionsOfInterest.get(index_r).getBids());
        allBidsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // set appropriate button and header text
        TextView displaySessionButtonTitle = (TextView) findViewById(R.id.sessionButton);
        TextView displaySessionHeaderTitle = (TextView) findViewById(R.id.bidsOnMySessionsText);
        displaySessionButtonTitle.setText("Return to " + sessionsOfInterest.get(index_r).getTitle());
        displaySessionHeaderTitle.setText("Viewing Bids on " + sessionsOfInterest.get(index_r).getTitle());

        final Button sessionButton = (Button) findViewById(R.id.sessionButton);
        sessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBidsActivity.this, ViewOneSessionActivity.class);
                intent.putExtra("index", index_r);
                startActivity(intent);
            }
        });

        allBidsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
                } else {
                    builder.setMessage("Accept Bid for $" + currentBid.getAmount() + "?")
                            .setCancelable(false)
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Integer index = sessions.indexOf(sessionsOfInterest.get(index_r));
                                    sessionsOfInterest.get(index_r).setStatus("booked");
                                    sessions.get(index).declineAllBids();
                                    for (int i = 0; i < sessions.get(index).getBids().size(); i++) {
                                        if (i==sessions.get(index).getBids().indexOf(currentBid)) {
                                            sessions.get(index).getBids().get(i).setStatus("accepted");
                                        } else {
                                            sessions.get(index).getBids().get(i).setStatus("declined");
                                        }
                                    }
                                    sessions.get(index).getBids().clear();
                                    sessions.get(index).getBids().add(currentBid);
                                    updateElasticSearchSession(sessionsOfInterest.get(index_r));
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                                    // Difference here is that the sessions status remains available
                            .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Integer index = sessions.indexOf(sessionsOfInterest.get(index_r));
                                    sessions.get(index).getBids().get(positionCopy).setStatus("declined");
                                    Bid tempObject = sessions.get(index).getBids().get(positionCopy);
                                    sessions.get(index).getBids().remove(tempObject);

                                    //TODO: notify bidder on decline
                                    //saveInFile(SESSIONSFILE, sessions);
                                    updateElasticSearchSession(sessions.get(index));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }
}