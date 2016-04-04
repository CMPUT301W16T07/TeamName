package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ViewOneUpcomingSession extends MethodsController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_one_upcoming_session);

        Intent intent = getIntent();

        final int index_r = intent.getIntExtra("index",0);
        final String index_receive = String.valueOf(index_r);
        //loadSessions(SESSIONSFILE);
        loadElasticSearch();
        initializeFields(index_r);

        Button upcomingOneBackButton = (Button) findViewById(R.id.upcomingOneBackButton);
        upcomingOneBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button rateTutorButton = (Button) findViewById(R.id.rateTutorButton);
        rateTutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"1","2","3","4","5"};
                //@Override
                //public Dialog onCreateDialog(Bundle savedInstanceState) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewOneUpcomingSession.this);
                    builder.setTitle("Rate Tutor")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Profile wantedProfile = getProfile(sessionsOfInterest.get(index_r).getTutorID());
                                    Double rating = (double)which + 1;
                                    wantedProfile.addTutorRating(rating);
                                    updateElasticSearchProfile(wantedProfile);
                                    loadElasticSearch();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
    }
    /**
     * initializeFields sets the texts of ViewOnUpcomingSession with the sessions information
     * @param index the index of the session in the sessions arraylist is passed in.
     */
    public void initializeFields(int index) {
        Profile tutor = getProfile(sessions.get(index).getTutorID());

        TextView titleBody = (TextView) findViewById(R.id.upcomingOneTitleBody);
        TextView descriptionBody = (TextView) findViewById(R.id.upcomingOneDescriptionBody);
        TextView postedByBody = (TextView) findViewById(R.id.upcomingOnePostedByBody);
        TextView bodyEmail = (TextView) findViewById(R.id.upcomingOneBodyEmail);
        TextView bodyPhone = (TextView) findViewById(R.id.upcomingOneBodyPhone);
        TextView bodyStatus = (TextView) findViewById(R.id.upcomingOneBodyStatus);

        ImageView sessionImage = (ImageView) findViewById(R.id.upcomingOneSessionImage);

        sessionImage.setImageBitmap(sessions.get(index).getThumbnail());
        titleBody.setText(Html.fromHtml("Title: <b>" + sessions.get(index).getTitle() + "</b>"));
        descriptionBody.setText(Html.fromHtml("Description: <b>"+sessions.get(index).getDescription() + "</b>"));
        postedByBody.setText(Html.fromHtml("Posted By: <b>"+tutor.getName() + "</b>"));
        bodyEmail.setText(Html.fromHtml("Email: <b> " + tutor.getEmail() + "</b>"));
        bodyPhone.setText(Html.fromHtml("Phone: <b>" + tutor.getPhone() + "</b>"));
        bodyStatus.setText(Html.fromHtml("Status: <b>You won the bid!</b>"));
    }

}
