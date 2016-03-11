package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ALI on 2016-03-09.
 */
public class MyProfileActivity extends MethodsController {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        btn_CurrentBids = (Button) findViewById(R.id.currentBids);
        btn_CurrentBids.setOnClickListener(btnClickListener);
        btn_myProfile = (Button) findViewById(R.id.myProfile);
        btn_myProfile.setOnClickListener(btnClickListener);
        btn_mySessions = (Button) findViewById(R.id.mySessions);
        btn_mySessions.setOnClickListener(btnClickListener);
        btn_availableSession = (Button) findViewById(R.id.availibleSessions);
        btn_availableSession.setOnClickListener(btnClickListener);



        //get textviews
        TextView displayUsername = (TextView) findViewById(R.id.username);
        TextView displayEmail = (TextView) findViewById(R.id.email);
        TextView displayPhone = (TextView) findViewById(R.id.phone);

        //set textviews
        displayUsername.setText("Username: " + currentProfile.getName());
        displayEmail.setText("Email: " + currentProfile.getEmail());
        displayPhone.setText("Phone: " + currentProfile.getPhone());

        //set click listener for edit button
        Button editButton = (Button) findViewById(R.id.editProfile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: make editProfileActivty.
                //DONE
               Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class );
               startActivity(intent);

            }
        });





    }
}
