package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import javax.net.ssl.SSLSessionBindingListener;

/**
 * Abrosda @12/2/2016
 * For the borrowing use cases
 */

public class BorrowTest extends ActivityInstrumentationTestCase2 {

    Instrumentation instrumentation;
    Activity activity;
    SessionList = (ListView) findViewById(R.id.borrowlist);
    private ArrayAdapter<SessionList> adapter;

    public BorrowTest(Class activityClass) {
        super(activityClass);
    }

    /**
     *US 06.01.01
     *As a borrower, I want to view a list of things I am borrowing, each thing with its description and owner username.
     */
    public void testBorrowedList() {
        Sessionlist sessions = new SessionList;
        User user = new User;
        Session testses = new Session("subject", "description", user);
        sessions.addSession(testses);
        assertTrue(sessions.get(0), testses);



        adapter.notifyDatasetchanged
        assertEquals(1, arrayAdapter.getCount());
        assertTrue(testses, arrayAdapter.getItem(arrayAdapter.getCount() - 1) instanceof Session);

    }

    /**
     * US 06.02.01
     *As an owner, I want to view a list of my things being borrowed, each thing with its description and borrower username
     */
    public void testBorrowingList() {
        Sessionlist sessions = new SessionList;
        User user = new User;
        User owner = new User;
        Session session = new Session("subject", "description", user);
        owner.addSession(session)
        session.setStatusBorrowed();
        sessions.addSession(session);

        assertTrue(sessions.get(0), session);

        adapter.notifyDatasetchanged
        assertEquals(1, arrayAdapter.getCount());
        assertTrue(session, arrayAdapter.getItem(arrayAdapter.getCount() - 1) instanceof Session);

    }
}