package com.teamname.tutortrader;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Tests that we can add photos to session
 * and that they are there
 */
public class PhotosTest extends ActivityInstrumentationTestCase2 {
    public PhotosTest() {
        super(AvailableSessionsActivity.class);
    }

    /* for use case photo 1*/
    public void testaddPhoto () {
        Profile profile = new Profile("Name", "Phone", "Email");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bm1 = Bitmap.createBitmap(1, 2, conf);
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels",profile.getProfileID(),bm1);
        ArrayList<Session> sessions = new ArrayList<Session>();
        sessions.add(session);
        assertEquals(sessions.get(0), session);
        assertEquals(session.getThumbnail(), bm1);
        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");
    }

}
