package com.teamname.tutortrader;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by taylorarnett on 2016-03-28.
 */
public class PhotosTest extends ActivityInstrumentationTestCase2 {
    public PhotosTest(Class activityClass) {
        super(activityClass);
    }

    /* for use case photo 1
    public void addPhototest () {
        AvailableSessionsActivity msa = (AvailableSessionsActivity)getActivity();
        Profile profile = new Profile("Name", "Phone", "Email");
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels",profile);
        ArrayList<Session> sessions = new ArrayList<Session>();
        sessions.add(session);
        assertEquals(sessions.get(0), session);
        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");

    }*/
}
