package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Abrosda @12/2/2016
 * US 03.02.01
 * As a user, I want to edit the contact information in my profile.
 */

public class ProfileTestTex extends ActivityInstrumentationTestCase2 {

    Instrumentation instrumentation;
    Activity activity;
    EditText username;
    EditText email;
    EditText phone;

    public ProfileTestText(Class activityClass) {
        super(activityClass);
    }
    public void testEditemail() {
        User user = new User;
        user.setEmail("randomemail@gmail.ca")
        assertTrue(user.getEmail(), "randomemail@gmail.ca")
    }
    public void testEditphon() {
        User user = new User;
        user.setPhone("7804737373");
        assertTrue(user.getPhone(),"7804737373")

    }
}