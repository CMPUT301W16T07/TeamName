package com.teamname.tutortrader;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by iali1 on 3/29/16.
 */
public class ViewOneSessionActivityTest extends ActivityInstrumentationTestCase2 {
    public ViewOneSessionActivityTest() {
        super(ViewOneSessionActivity.class);
    }

    @UiThreadTest
    public void testViewOneSession() {
        ViewOneSessionActivity vosa = (ViewOneSessionActivity) getActivity();
        //assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.));

        Profile newProfile = new Profile("Dude", "man", "222");
        Session newSession = new Session("Math", "Tutor for linear Algebra for all university levels", newProfile.getProfileID());
        ArrayList<Session> sessions = new ArrayList<Session>();

        sessions.add(newSession);

        ListView theSessions = (ListView) vosa.findViewById(R.id.sessionList);
        ArrayAdapter<Session> adapter;
        adapter = new AvailableSessionsAdapter(vosa, sessions);
        theSessions.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // http://blog.denevell.org/android-instrumentation-click-list.html accessed 02-2016-12
        theSessions.performItemClick(theSessions, 0, theSessions.getItemIdAtPosition(0));

        //testing the fields
        TextView subjectTitle = (TextView) vosa.findViewById(R.id.titleBody);
        assertEquals("Math", subjectTitle.getText().toString());
        TextView sessionDescription = (TextView) vosa.findViewById(R.id.descriptionBody);
        assertEquals("Tutor for linear Algebra for all university levels",
                sessionDescription.getText().toString());
        TextView biddingStatus = (TextView) vosa.findViewById(R.id.bodyStatus);
        assertTrue((biddingStatus.getText().toString() == "available") ||
                (biddingStatus.getText().toString() == "closed") ||
                (biddingStatus.getText().toString() == "pending"));

        // test if buttons are present
        assertNotNull(vosa.findViewById(com.teamname.tutortrader.R.id.allSessionsButton));
        assertNotNull(vosa.findViewById(com.teamname.tutortrader.R.id.editButton));
        assertNotNull(vosa.findViewById(com.teamname.tutortrader.R.id.deleteButton));
        assertNotNull(vosa.findViewById(R.id.viewBidsButton));

    }

}
