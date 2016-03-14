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
 * Created by abrosda on 3/14/16.
 */
public class
        BidOnSessionActivity extends MethodsController {


    /**
     * Loads the selected session from the list and displays the information
     * User can put an amount in the bid then hit the bid button
     * this should save the bid into the list of bids on the session
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        /*
            The index of the entry that was clicked in the list of entries displayed on the main
            screen is passed through the intent. Here is where we access it
        */
        String index_receive = intent.getStringExtra("index");
        final int index = Integer.parseInt(index_receive);
        loadSessions(SESSIONSFILE);
        final Session selectedSession = sessions.get(index);
        //initializeFields(selectedSession);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_on_session);
        Button backToAllButton = (Button) findViewById(R.id.allSessionsButton);
        backToAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidOnSessionActivity.this, AvailableSessionsActivity.class);
                startActivity(intent);
            }
        });

        Button bidButton = (Button) findViewById(R.id.bidButton);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float bidvalue = 0;
                try {
                    EditText bidtext = ((EditText) findViewById(R.id.bidtext));
                    /**
                     * taken from http://stackoverflow.com/questions/4229710/string-from-edittext-to-float
                     */
                    bidvalue = Float.valueOf(bidtext.getText().toString());
                    UUID id = currentProfile.getProfileID();
                    Bid newbid = new Bid(id, bidvalue);
                    selectedSession.addBid(newbid);
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
     * @param theSession index of the session in the sessions arraylist is passed in.
     */
    public void initializeFields(Session theSession) {
        TextView subjectText = (TextView) findViewById(R.id.subjectTextB);
        TextView titleBody = (TextView) findViewById(R.id.titleBodyB);
        TextView descriptionBody = (TextView) findViewById(R.id.descriptionBodyB);
        TextView postedByBody = (TextView) findViewById(R.id.postedByBodyB);
        TextView bodyEmail = (TextView) findViewById(R.id.bodyEmailB);
        TextView bodyPhone = (TextView) findViewById(R.id.bodyPhoneB);
        TextView bodyStatus = (TextView) findViewById(R.id.bodyStatusB);

        subjectText.setText(theSession.getTitle());
        titleBody.setText("Title: " + theSession.getTitle());
        descriptionBody.setText("Description: "+theSession.getDescription());
        postedByBody.setText("Posted By: " + theSession.tutor.getName());
        bodyEmail.setText("Email: " + theSession.tutor.getEmail());
        bodyPhone.setText("Phone" + theSession.tutor.getPhone());
        bodyStatus.setText("Status: " + theSession.getStatus());


    }
}
