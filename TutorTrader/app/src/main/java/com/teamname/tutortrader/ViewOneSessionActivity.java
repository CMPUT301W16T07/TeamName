package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The activity that allows users to view information about a
 * single session that they've created.
 */
public class ViewOneSessionActivity extends MethodsController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_one_session);
        Intent intent = getIntent();
        /*
            The index of the entry that was clicked in the list of entries displayed on the main
            screen is passed through the intent. Here is where we access it
        */
        final Integer index_r = intent.getIntExtra("index", 0);

        loadElasticSearch();
        initializeFields(index_r);

        Button allSessionsButton = (Button) findViewById(R.id.allSessionsButton);
        allSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneSessionActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneSessionActivity.this, EditSessionActivity.class);
                intent.putExtra("index", index_r);
                startActivity(intent);
            }
        });
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        // from http://stackoverflow.com/questions/5116027/how-to-create-a-prompt-dialogue 02-10-2016
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewOneSessionActivity.this);
                builder.setMessage("Are you sure you would like to delete this session?")
                        .setCancelable(false)
                        // This will delete the session of interest
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ElasticSearchController.RemoveSessionTask removeSessionTask = new ElasticSearchController.RemoveSessionTask();
                                removeSessionTask.execute(sessionsOfInterest.get(index_r).getSessionID());
                                loadElasticSearch();
                                Intent intent = new Intent(ViewOneSessionActivity.this, MySessionsActivity.class);
                                startActivity(intent);
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
        });

        Button viewBidsButton = (Button) findViewById(R.id.viewBidsButton);
        viewBidsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneSessionActivity.this, ViewBidsActivity.class);
                intent.putExtra("index", index_r);
                startActivity(intent);
            }
        });


        Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity && (sessionsOfInterest.get(index_r).getLocation() != null)){
                    Intent intent = new Intent(ViewOneSessionActivity.this, ViewMapsActivity.class);

                    Bundle place = new Bundle();
                    place.putParcelable("place", sessionsOfInterest.get(index_r).getLocation());
                    intent.putExtras(place);
                    startActivity(intent);
                }else{
                    Toast.makeText(ViewOneSessionActivity.this, "No Location Available", Toast.LENGTH_LONG).show();
                }

            }
        });


        Button repostButton = (Button) findViewById(R.id.repostButton);
        repostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewOneSessionActivity.this);
                builder.setMessage("Reposting this session means all bids will be removed and the status will be set to available. Do you wish to continue?")
                .setCancelable(false)
                        // This will repost the session of interest
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sessionsOfInterest.get(index_r).declineAllBids();
                                sessionsOfInterest.get(index_r).deleteAllBids();
                                sessionsOfInterest.get(index_r).setStatus("available");
                                updateElasticSearchSession(sessionsOfInterest.get(index_r));
                                loadElasticSearch();
                                Intent intent = new Intent(ViewOneSessionActivity.this, MySessionsActivity.class);
                                startActivity(intent);
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
        });

        Button rateStudentButton = (Button) findViewById(R.id.rateStudentButton);
        rateStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionsOfInterest.get(index_r).getStatus().equals("booked")) {
                    final CharSequence[] items = {"1","2","3","4","5"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewOneSessionActivity.this);
                    builder.setTitle("Rate Student")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // get the winning bid
                                    Bid winningBid = null;
                                    // to get the accepted bidders ID
                                    winningBid = sessionsOfInterest.get(index_r).getBids().get(0);
                                    Profile studentProfile = getProfile(winningBid.getBidder());
                                    Double rating = (double)which + 1;
                                    studentProfile.addStudentRating(rating);
                                    updateElasticSearchProfile(studentProfile);
                                    loadElasticSearch();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    //Learned from http://developer.android.com/guide/topics/ui/notifiers/toasts.html
                    Context context = getApplicationContext();
                    CharSequence text = "Rating can only be done once session is booked!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }

        });
    }

    /**
     * initializeFields sets the texts of ViewOneSession with the sessions information
     * @param index the index of the session in the sessions arraylist is passed in.
     */
    public void initializeFields(int index) {
        Profile tutor = getProfile(sessionsOfInterest.get(index).getTutorID());

        TextView titleBody = (TextView) findViewById(R.id.titleBody);
        TextView descriptionBody = (TextView) findViewById(R.id.descriptionBody);
        TextView postedByBody = (TextView) findViewById(R.id.postedByBody);
        TextView bodyEmail = (TextView) findViewById(R.id.bodyEmail);
        TextView bodyPhone = (TextView) findViewById(R.id.bodyPhone);
        TextView bodyStatus = (TextView) findViewById(R.id.bodyStatus);

        ImageView sessionImage = (ImageView) findViewById(R.id.sessionImage);

        sessionImage.setImageBitmap(sessionsOfInterest.get(index).getThumbnail());

        titleBody.setText(Html.fromHtml("Title: <b>" + sessionsOfInterest.get(index).getTitle() + "</b>"));
        descriptionBody.setText(Html.fromHtml("Description: <b>"+sessionsOfInterest.get(index).getDescription() + "</b>"));
        postedByBody.setText(Html.fromHtml("Posted By: <b>"+ tutor.getName() + "</b>"));
        bodyEmail.setText(Html.fromHtml("Email: <b>" + tutor.getEmail() + "</b>"));
        bodyPhone.setText(Html.fromHtml("Phone: <b>" + tutor.getPhone() + "</b>"));
        bodyStatus.setText(Html.fromHtml("Status: <b>"+sessionsOfInterest.get(index).getStatus() + "</b>"));
    }
}
