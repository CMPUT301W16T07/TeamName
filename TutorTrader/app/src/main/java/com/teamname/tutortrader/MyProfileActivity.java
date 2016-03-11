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

        //get profile singleton
        Profile currentProfile = Profile.getInstance();

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
               // Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class );
               // startActivity(intent);

            }
        });





    }
}
