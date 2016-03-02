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

    public ProfileTestText(Class activityClass) {
        super(activityClass);
    }

    public void testEditEmail() {
        Profile user = new Profile();
        user.setEmail("randomemail@gmail.ca");
        assertEquals(user.getEmail(), "randomemail@gmail.ca");
    }

    public void testEditPhone() {
        Profile user = new Profile();
        user.setPhone("7804737373");
        assertEquals(user.getPhone(),"7804737373");

    }



    /**
     *  added by abrosda
     *  US 03.03.01
     * As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.
     */
    public void testShowUser() {
        TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
        User user = new User;
        session = createSession("Math", "Tutor for highschool math classes.");
        session.setUser(user);
        assertTrue(session.getuser(), user );

    }
}