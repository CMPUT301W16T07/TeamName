package com.teamname.tutortrader;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by taylorarnett on 2016-02-10.
 */
public class TutorTraderActivityTest extends ActivityInstrumentationTestCase2 {
    public TutorTraderActivityTest(Class activityClass) {
        super(activityClass);
    }
    /**
     * Testing "Things" Use Cases
     */

    /**
     * Testing UseCase 01.01.01 - AddSession
     * "As an owner, I want to add a thing in my things, each denoted with a clear, suitable description."
     */
    public void testAddSessionValid() {
        SessionList sessions = new SessionList();

        assertFlase(sessions.hasSession(session));

        Session session = new Session("Math","Tutor for linear Algebra for all university levels");
        sessions.addSession(session);

        assertTrue(sessions.hasSession(session));

    }

    /**
     * This tests use case 1 but in the case where the input fields are incomplete
     */
    public void testAddSessionIncomplete () {

    }
}
