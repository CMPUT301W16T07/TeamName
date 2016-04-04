package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        checkForSelf(selectedSession);
        initializeFields(selectedSessionIndex);

        ImageView viewImage = (ImageView) findViewById(R.id.sessionImage);
        viewImage.setImageBitmap(sessions.get(index).getThumbnail());

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.bid_on_session);
        Button backToAllButton = (Button) findViewById(R.id.allSessionsButton);
        backToAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Intent intent = new Intent(BidOnSessionActivity.this, AvailableSessionsActivity.class);
                //startActivity(intent);
            }
        });

        Button bidButton = (Button) findViewById(R.id.bidButton);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentProfile.isDefaultUser()) {
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
                        Profile owner = MethodsController.getProfile(selectedSession.getTutorID());
                        owner.setNewBid(true);

                        //updates the profile of elastic search
                        updateElasticSearchProfile(owner);

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
                } else {
                    verifyLogin();
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

        titleBody.setText(Html.fromHtml("Title: <b>" + sessions.get(index).getTitle() + "</b>"));
        descriptionBody.setText(Html.fromHtml("Description: <b>"+sessions.get(index).getDescription() + "</b>"));
        postedByBody.setText(Html.fromHtml("Posted By: <b>"+ tutor.getName() + "</b>"));
        tutorRatingBody.setText(Html.fromHtml("Tutor Rating: <b>"+ tutor.getTutorRating() + "</b>"));
        bodyEmail.setText(Html.fromHtml("Email: <b>" + tutor.getEmail() + "</b>"));
        bodyPhone.setText(Html.fromHtml("Phone: <b>" + tutor.getPhone() + "</b>"));
        bodyStatus.setText(Html.fromHtml("Status: <b>"+sessions.get(index).getStatus() + "</b>"));



    }

    /**
     * checks if the selected session is your own session
     * @param selectedSession
     */
    public void checkForSelf(Session selectedSession) {
        Profile tutor = getProfile(selectedSession.getTutorID());
        if (tutor == null) throw new AssertionError();
        if (tutor.getProfileID().equals(currentProfile.getProfileID())) {
            //Learned from http://developer.android.com/guide/topics/ui/notifiers/toasts.html
            Context context = getApplicationContext();
            CharSequence text = "You cannot bid on your own session!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            finish();
        }
    }
}
