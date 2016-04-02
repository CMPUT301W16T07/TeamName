package com.teamname.tutortrader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * The activity that allows a user to edit a session
 * that they previously created.
 */
public class EditSessionActivity extends MethodsController {
//Bitmap thumbnail;
    Integer sessions_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_session);

        Intent intent = getIntent();
        final String index_receive = intent.getStringExtra("index");
        final int index_r = Integer.parseInt(index_receive);
        initializeFields(index_r);
        sessions_index = sessions.indexOf(sessionsOfInterest.get(index_r));

        newImage = (ImageView)findViewById(R.id.editImageView);

        // to take a new photo and overwrite the old
        Button newPhotoButton = (Button) findViewById(R.id.newPhotoButton);
        newPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                }
            }
        });

        //this just removes the reference to the photo, and garbage collection will do the rest.
        Button deletePhotoButton = (Button) findViewById(R.id.deletePhotoButton);
        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbnail = null;
                newImage.setImageBitmap(thumbnail);

            }
        });

        //cancel edit session will not save any of the changes and it will return to the My Sessions tab
        Button cancelEditSession = (Button) findViewById(R.id.cancelEditSession);
        cancelEditSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSessionActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid;
                EditText subjectEdit = (EditText) findViewById(R.id.subjectEdit);
                EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
                valid = verifyFields();
                if (valid) {
                    // Remove old session
                    ElasticSearchController.RemoveSessionTask removeSessionTask = new ElasticSearchController.RemoveSessionTask();
                    removeSessionTask.execute(sessionsOfInterest.get(index_r).getSessionID());

                    Session newSession = new Session(subjectEdit.getText().toString(),descriptionEdit.getText().toString(),currentProfile.getProfileID(),thumbnail);
                    newSession.addThumbnail(thumbnail); //must add this line to properly attach image

                    ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
                    addSessionTask.execute(newSession);
                    loadElasticSearch(); // load the newest addition
                    finish();
                }
            }
        });
    }



    /**
     * checks to see if the input fields are valid
     * @return boolean variable true if valid, false if invalid
     */
    public boolean verifyFields () {
        //Boolean validFields = false;
        EditText subjectEdit = (EditText) findViewById(R.id.subjectEdit);
        EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);

        if ((!subjectEdit.getText().toString().equals("")) && (!descriptionEdit.getText().toString().equals(""))) {
            return true;
        }
        return false;
    }

    /**
     * initializeFields sets the texts of EditSession with the sessions information
     *
     * @param index the index of the session in the sessions arraylist is passed in.
     */
    public void initializeFields(int index) {
        EditText subjectEdit = (EditText) findViewById(R.id.subjectEdit);
        EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
        ImageView sessionImage = (ImageView) findViewById(R.id.editImageView);

        sessionImage.setImageBitmap(sessionsOfInterest.get(index).getThumbnail());
        subjectEdit.setText(sessionsOfInterest.get(index).getTitle());
        descriptionEdit.setText(sessionsOfInterest.get(index).getDescription());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            Bundle extras = data .getExtras();
            thumbnail = (Bitmap)extras.get("data");
            sessions.get(sessions_index).addThumbnail(thumbnail);
            updateElasticSearchSession(sessions.get(sessions_index));
            //saveInFile(SESSIONSFILE, sessions);
            newImage.setImageBitmap(sessions.get(sessions_index).getThumbnail());
        }
    }
}