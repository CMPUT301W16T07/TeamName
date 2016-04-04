package com.teamname.tutortrader;

// getting rid of unused imports
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

import java.util.ArrayList;


/**
 *
 * The activity for adding new tutor sessions as a user.
 */
public class AddSessionActivity extends MethodsController  {


   private LatLng tempPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_session);

        Button cancelAddSession = (Button) findViewById(R.id.cancelAddSession);
        cancelAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSessionActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });
        newImage = (ImageView)findViewById(R.id.newImage);
        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid;
                valid = verifyFields();
                if (valid) {
                    EditText subjectEdit = (EditText) findViewById(R.id.subjectEdit);
                    EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
                    // TODO: implement Tutor

                    Session newSession = new Session(subjectEdit.getText().toString(),descriptionEdit.getText().toString(),currentProfile.getProfileID(), thumbnail, tempPoint);

                    newSession.addThumbnail(thumbnail);



                    //sessions.add(newSession);
                    if (Connectivity) {
                        ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
                        addSessionTask.execute(newSession);
                    }else{
                            ArrayList<Session> tempSessions = loadOffline();
                            tempSessions.add(newSession);
                            saveInFile(OFFLINEFILE, tempSessions);
                            Toast.makeText(AddSessionActivity.this, "Session will be uploaded once internet is connected", Toast.LENGTH_LONG).show();
                    }
                    loadElasticSearch(); // load the newest addition


                    Intent intent = new Intent(AddSessionActivity.this, MySessionsActivity.class);
                    startActivity(intent);

                }
            }
        });

        Button LocationButton = (Button)findViewById(R.id.AddLocationButton);
        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Connectivity) {
                    Intent intent = new Intent(AddSessionActivity.this, MapsActivity.class);
                    startActivityForResult(intent, REQUEST_LOCATION);
                }else{
                    Toast.makeText(AddSessionActivity.this,"You need internet to add a location.",Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            Bundle extras = data .getExtras();
            thumbnail = (Bitmap)extras.get("data");
            newImage.setImageBitmap(thumbnail);
        }
        if(requestCode == REQUEST_LOCATION && resultCode == RESULT_OK){
           Bundle extras = data.getExtras();
            tempPoint = extras.getParcelable("point");

        }
    }

}
