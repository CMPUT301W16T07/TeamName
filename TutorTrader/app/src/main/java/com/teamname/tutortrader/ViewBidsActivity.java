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
        final String index_receive = intent.getStringExtra("index");
        final int index_r = Integer.parseInt(index_receive);

        // populates the list of all bids
        allBidsList = (ListView) findViewById(R.id.bidsListView);
        allBidsList.setBackgroundResource(R.drawable.black_chalkboard);
        //loadSessions(SESSIONSFILE);
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
                finish();
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
                                    Session tempSession = sessions.get(index);
                                    tempSession.setStatus("booked");
                                    updateElasticSearch(tempSession);
                                    //availableSessions.remove(index);
                                    notifyBidders(currentBid, index);
                                    //saveInFile(SESSIONSFILE, sessions);
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
                                    updateElasticSearch(sessions.get(index));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


    }



    /**
     * notifyBidders will notify all bidders who were declined, and remove those bids from the sessions
     * bids. It will then notify the successful bidder and keep it in the list.
     * //@param bidsList the list of bids for the session of interest
     * @param currentBid the bid object of the accepted bid within a sessions bids
     * @param sessionIndex the index of the session of interest in the list of all sessions.
     */
    private void notifyBidders(Bid currentBid, Integer sessionIndex) {
        //sessions.get(sessionIndex).getBids().size()
        Session tempSession = sessions.get(sessionIndex);
        Bid acceptedBid = currentBid;
        acceptedBid.setStatus("accepted");
        tempSession.declineAllBids();
        for (int i = 0; i < tempSession.getBids().size();i++) {
            if (i==tempSession.getBids().indexOf(currentBid)) {
                tempSession.getBids().get(i).setStatus("accepted");
                acceptedBid = tempSession.getBids().get(i);

                //TODO: notify winning bidder
            } else {
                // TODO: alert bidder that it has been declined
                tempSession.getBids().get(i).setStatus("declined"); // probably dont need this line
               // sessions.get(sessionIndex).getBids().remove(sessions.get(sessionIndex).getBids().get(i));
            }

        }
        tempSession.getBids().clear();
        tempSession.getBids().add(acceptedBid);
        updateElasticSearch(tempSession);
        //saveInFile(SESSIONSFILE, sessions);
    }
}