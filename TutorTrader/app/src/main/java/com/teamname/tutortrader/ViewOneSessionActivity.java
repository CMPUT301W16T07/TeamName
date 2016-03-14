package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        String index_receive = intent.getStringExtra("index");
        final int index_r = Integer.parseInt(index_receive);
        loadSessions(SESSIONSFILE);
        initializeFields(index_r);

        Button allSessionsButton = (Button) findViewById(R.id.allSessionsButton);
        allSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: we should pass the data entry so the fields can be filled in
                Intent intent = new Intent(ViewOneSessionActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: we should pass the data entry so the fields can be filled in
                Intent intent = new Intent(ViewOneSessionActivity.this, EditSessionActivity.class);
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
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sessions.remove(index_r);
                                saveInFile(SESSIONSFILE,sessions);
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
    }

    /**
     * initializeFields sets the texts of ViewOneSession with the sessions information
     * @param index the index of the session in the sessions arraylist is passed in.
     */
    public void initializeFields(int index) {
        TextView subjectText = (TextView) findViewById(R.id.subjectText);
        TextView titleBody = (TextView) findViewById(R.id.titleBody);
        TextView descriptionBody = (TextView) findViewById(R.id.descriptionBody);
        TextView postedByBody = (TextView) findViewById(R.id.postedByBody);
        TextView bodyEmail = (TextView) findViewById(R.id.bodyEmail);
        TextView bodyPhone = (TextView) findViewById(R.id.bodyPhone);
        TextView bodyStatus = (TextView) findViewById(R.id.bodyStatus);

        subjectText.setText(sessions.get(index).getTitle());
        titleBody.setText("Title: "+ sessions.get(index).getTitle());
        descriptionBody.setText("Description: "+sessions.get(index).getDescription());
        postedByBody.setText("Posted By: "+sessions.get(index).tutor.getName());
        bodyEmail.setText("Email: " + sessions.get(index).tutor.getEmail());
        bodyPhone.setText("Phone" +sessions.get(index).tutor.getPhone());
        bodyStatus.setText("Status: "+sessions.get(index).getStatus());


    }
}
