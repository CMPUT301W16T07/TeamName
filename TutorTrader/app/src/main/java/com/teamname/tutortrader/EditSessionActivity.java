package com.teamname.tutortrader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * The activity that allows a user to edit a session
 * that they previously created.
 */
public class EditSessionActivity extends MethodsController {

    Integer sessions_index;
    private LatLng tempPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_session);

        Intent intent = getIntent();
        final Integer index_r = intent.getIntExtra("index", 0);

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

                    sessionsOfInterest.get(index_r).setTitle(subjectEdit.getText().toString());
                    sessionsOfInterest.get(index_r).setDescription(descriptionEdit.getText().toString());
                    updateElasticSearchSession(sessionsOfInterest.get(index_r));
                    Intent intent = new Intent(EditSessionActivity.this, MySessionsActivity.class);
                    startActivity(intent);

                }
            }
        });

        Button LocationButton = (Button)findViewById(R.id.newLocationButton);
        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity && (sessionsOfInterest.get(index_r).getLocation() != null)){
                    Intent intent = new Intent(EditSessionActivity.this, MapsActivity.class);
                    startActivityForResult(intent, REQUEST_LOCATION);
                }else{
                    Toast.makeText(EditSessionActivity.this, "You need internet to add a location.", Toast.LENGTH_LONG).show();
                }


            }
        });

        Button LocationDeleteButton = (Button) findViewById(R.id.deleteLocationButton);
        LocationDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity){
                    sessionsOfInterest.get(index_r).setLocation(null);

                }else{
                    Toast.makeText(EditSessionActivity.this, "You need internet to delete a location.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    /**
     * checks to see if the input fields are valid
     * @return boolean variable true if valid, false if invalid
     */
    public boolean verifyFields () {
        EditText subjectEdit = (EditText) findViewById(R.id.subjectEdit);
        EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);

        return ((!subjectEdit.getText().toString().equals("")) && (!descriptionEdit.getText().toString().equals("")));
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
            newImage.setImageBitmap(sessions.get(sessions_index).getThumbnail());
        }

        if(requestCode == REQUEST_LOCATION && resultCode == RESULT_OK){
            if(data.hasExtra("point")){
                Bundle extras = data.getExtras();
                tempPoint = (LatLng) extras.get("point");
            }
        }
    }
}