package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

/**
 *
 * The activity for bidding on existing, available sessions.
 */
public class BidOnSessionActivity extends MethodsController {

    Session selectedSession;
    Integer selectedSessionIndex;

    /**
     * Loads the selected session from the list and displays the information
     * User can put an amount in the bid then hit the bid button
     * this should save the bid into the list of bids on the session
     * @param savedInstanceState this is the saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_on_session);
        Intent intent = getIntent();
        /*
            The index of the entry that was clicked in the list of entries displayed on the main
            screen is passed through the intent. Here is where we access it
        */
        final String index_receive = intent.getStringExtra("index");
        final int index = Integer.parseInt(index_receive);
        loadElasticSearch();
        //loadSessions(SESSIONSFILE);
        selectedSessionIndex = sessions.indexOf(availableSessions.get(index));
        selectedSession = sessions.get(selectedSessionIndex);
        initializeFields(selectedSessionIndex);


        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.bid_on_session);
        Button backToAllButton = (Button) findViewById(R.id.allSessionsButton);
        backToAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidOnSessionActivity.this, AvailableSessionsActivity.class);
                startActivity(intent);
            }
        });

        Button viewImageButton = (Button) findViewById(R.id.viewImageButton);
        viewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidOnSessionActivity.this, ViewImageActivity.class);
                // TODO: must pass the index so view image activity can return to proper bidonsession
                intent.putExtra("index", String.valueOf(selectedSessionIndex));
                startActivity(intent);
            }
        });

        Button bidButton = (Button) findViewById(R.id.bidButton);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float bidvalue;

                try {
                    EditText bidtext = ((EditText) findViewById(R.id.bidtext));
                    /**
                     * taken from http://stackoverflow.com/questions/4229710/string-from-edittext-to-float
                     */
                    bidvalue = Float.valueOf(bidtext.getText().toString());
                    UUID profileID = currentProfile.getProfileID();
                    Bid newbid = new Bid(selectedSession.getSessionID(), profileID, bidvalue);
                    selectedSession.addBid(newbid);
                    selectedSession.setStatus("Pending");

                    //ElasticSessionController.AddSessionTask addSessionTask = new ElasticSessionController.AddSessionTask();
                    //addSessionTask.execute(selectedSession);
                    //TODO: update Elastic Search, we have the code to add session bt we need to remove session. Create a removeSessionTask.
                    //saveInFile(SESSIONSFILE, sessions);
                    updateElasticSearchSession(selectedSession); // to add the newest bid
                    Intent intent = new Intent(BidOnSessionActivity.this, AvailableSessionsActivity.class);
                    startActivity(intent);
                } catch (Exception err) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(BidOnSessionActivity.this);
                    builder1.setMessage("Error invalid input, please use a numerical value");
                    builder1.setCancelable(true);

                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                }



            }
        });
    }


    /**
     * initializeFields sets the texts of ViewOneSession with the sessions information
     * @param index index of the session in the sessions arraylist is passed in.
     */
    public void initializeFields(int index) {
        Profile tutor = getProfile(sessions.get(index).getTutorID());

        TextView subjectText = (TextView) findViewById(R.id.subjectTextB);
        TextView titleBody = (TextView) findViewById(R.id.titleBodyB);
        TextView descriptionBody = (TextView) findViewById(R.id.descriptionBodyB);
        TextView postedByBody = (TextView) findViewById(R.id.postedByBodyB);
        TextView tutorRatingBody = (TextView) findViewById(R.id.tutorRatingB);
        TextView bodyEmail = (TextView) findViewById(R.id.bodyEmailB);
        TextView bodyPhone = (TextView) findViewById(R.id.bodyPhoneB);
        TextView bodyStatus = (TextView) findViewById(R.id.bodyStatusB);

        subjectText.setText(sessions.get(index).getTitle());
        titleBody.setText("Title: "+ sessions.get(index).getTitle());
        descriptionBody.setText("Description: "+sessions.get(index).getDescription());
        postedByBody.setText("Posted By: "+ tutor.getName());
        tutorRatingBody.setText("Tutor Rating: "+ tutor.getTutorRating());
        bodyEmail.setText("Email: " + tutor.getEmail());
        bodyPhone.setText("Phone: " + tutor.getPhone());
        bodyStatus.setText("Status: "+sessions.get(index).getStatus());


    }
}
