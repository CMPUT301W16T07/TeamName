package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The activity that allows a user to view their profile.
 */
public class MyProfileActivity extends MethodsController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        checkConnectivity();

        btn_CurrentBids = (Button) findViewById(R.id.currentBids);
        btn_CurrentBids.setOnClickListener(btnClickListener);
        btn_myProfile = (Button) findViewById(R.id.myProfile);
        btn_myProfile.setOnClickListener(btnClickListener);
        btn_mySessions = (Button) findViewById(R.id.mySessions);
        btn_mySessions.setOnClickListener(btnClickListener);
        btn_availableSession = (Button) findViewById(R.id.availableSessions);
        btn_availableSession.setOnClickListener(btnClickListener);

        // set activity title
        TextView activityTitle = (TextView) findViewById(R.id.activityTitle);
        activityTitle.setText(R.string.MyProfileButton);


        if (currentProfile.isDefaultUser()){
            //create new profile
            Intent intent = new Intent(MyProfileActivity.this, CreateProfileActivity.class);
            startActivity(intent);
        }

        if ((Connectivity)&&(getProfile(currentProfile.getProfileID()) != null)) {
            setCurrentProfile(getProfile(currentProfile.getProfileID())); // in case ratings changed
        }

        //get textviews
        TextView displayUsername = (TextView) findViewById(R.id.username);
        TextView displayEmail = (TextView) findViewById(R.id.email);
        TextView displayPhone = (TextView) findViewById(R.id.phone);
        TextView displayTutorRating = (TextView) findViewById(R.id.tutorRating);
        TextView displayStudentRating = (TextView) findViewById(R.id.studentRating);

        //set textviews
        displayUsername.setText("Username: " + currentProfile.getName());
        displayEmail.setText("Email: " + currentProfile.getEmail());
        displayPhone.setText("Phone: " + currentProfile.getPhone());
        displayTutorRating.setText("Tutor Rating: " + currentProfile.getTutorRating());
        displayStudentRating.setText("Student Rating: " + currentProfile.getStudentRating());

        //set click listener for edit button
        Button editButton = (Button) findViewById(R.id.editProfile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnectivity();
               Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class );
               startActivity(intent);

            }
        });
    }
}
