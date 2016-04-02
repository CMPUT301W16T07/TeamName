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
 *
 * For profile tests.
 */

public class ProfileTest extends ActivityInstrumentationTestCase2 {

    Instrumentation instrumentation;
    Activity activity;
    EditText username;
    EditText email;
    EditText phone;

    public ProfileTest() {
        super(AvailableSessionsActivity.class);
    }


    public void testEditEmail() {
        Profile user = new Profile("john","123456","poop");
        user.setEmail("randomemail@gmail.ca");
        assertEquals(user.getEmail(), "randomemail@gmail.ca");
    }

    public void testEditPhone() {
        Profile user = new Profile("john","this should be changed","randomemail@email.email");
        user.setPhone("7804737373");
        assertEquals(user.getPhone(),"7804737373");
    }


}