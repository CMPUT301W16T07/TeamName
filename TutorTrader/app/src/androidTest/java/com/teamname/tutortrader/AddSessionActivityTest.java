package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by abrosda on 3/14/16.
 */
public class AddSessionActivityTest extends ActivityInstrumentationTestCase2 {


    Instrumentation instrumentation;
    Activity activity;
    EditText titleInput;
    EditText descriptionInput;
    EditText searchInput;
    public AddSessionActivityTest() {
        super(AvailableSessionsActivity.class);
    }

    /**
     * Testing "Things" Use Cases
     */

    /**
     * Testing UseCase 01.01.01 - AddSession
     * "As an owner, I want to add a thing in my things, each denoted with a clear,
     *  suitable description."
     */

    /** USECASE 1 - AddSession
     *  createSession(title, description) fills in the input text field and
     *  clicks the 'save' button for the activity under test:
     */
    public void testAddSessionValid() {
        AvailableSessionsActivity msa = (AvailableSessionsActivity)getActivity();
        Profile profile = new Profile("Name", "Phone", "Email");
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels",profile);
        ArrayList<Session> sessions = new ArrayList<Session>();
        sessions.add(session);
        assertEquals(sessions.get(0), session);

        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");
    }



}