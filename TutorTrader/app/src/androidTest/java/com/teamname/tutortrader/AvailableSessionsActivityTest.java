package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.robotium.solo.Solo;

/**
 *
 * For tests dealing with sessions.
 */
public class AvailableSessionsActivityTest extends ActivityInstrumentationTestCase2<AvailableSessionsActivity> {

    private Solo solo;

    public AvailableSessionsActivityTest() {
        super(AvailableSessionsActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        //setUp() is run before a test case is started.
        //This is where the solo object is create   d.
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test cas    e has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
        super.tearDown();
    }


    /**
     * Testing UseCase 01.02.01 - ViewSessions
     * "As an owner, I want to view a list of all my sessions, and their descriptions and statuses."
     * <p/>
     * To test, we add 2 new sessions and then we check if they are there
     * We assume that sessions.add works
     * @see AddSessionActivityTest
     */

    public void testViewSessions() throws Exception {
        AvailableSessionsActivity tta = (AvailableSessionsActivity) getActivity();
        solo.waitForActivity("AvailableSessionsActivity",2000);
        solo.assertCurrentActivity("wrong act", AvailableSessionsActivity.class);
        assertNotNull(tta.findViewById(R.id.mySessions));

        ArrayList<Session> sessions = new ArrayList<Session>();
        Profile profile = new Profile("Name", "Phone", "Email");
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels", profile.getProfileID());
        Session session2 = new Session("Stats", "Tutor for Stats 252 and 141", profile.getProfileID());


        sessions.add(session);
        sessions.add(session2);

        ListView theSessions = (ListView) tta.findViewById(R.id.sessionList);
        ArrayAdapter adapter;
        adapter = new AvailableSessionsAdapter(solo.getCurrentActivity(), sessions);
        theSessions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // To test that two sessions show up
        assertEquals(sessions.size(), 2);
        assertTrue(solo.searchText("Math"));
        assertNotNull("There is the math session",
                theSessions.getItemIdAtPosition(0));
        assertNotNull("There is a stats session",
                theSessions.getItemAtPosition(1));
    }


    /**
     * Testing UseCase 01.04.01 - ViewOneSession
     * "As an owner, I want to view one of my things, its description and status."
     * <p/>
     * We will perform a click on list entry to bring us to the ViewOneSession view.
     * From here we test to see that all the buttons are present, and all the TextViews are
     * accurate
     */
    @UiThreadTest
    public void testViewOneSession() {
        solo.assertCurrentActivity("right activity", AvailableSessionsActivity.class);
        AvailableSessionsActivity tta = (AvailableSessionsActivity) getActivity();
        //assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.));

        Profile newProfile = new Profile("Dude", "man", "222");
        Session newSession = new Session("Math", "Tutor for linear Algebra for all university levels", newProfile.getProfileID());
        ArrayList<Session> sessions = new ArrayList<Session>();

        ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
        addSessionTask.execute(newSession);
        solo.clickOnMenuItem("Available");

        // http://blog.denevell.org/android-instrumentation-click-list.html accessed 02-2016-12
        //solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_save));
        solo.assertCurrentActivity("still good", AvailableSessionsActivity.class);
        //solo.waitForText("Math");
        //assertNotNull("There is the math session",
        //        theSessions.getItemIdAtPosition(0));

//        solo.clickOnButton(R.id.myProfile);
        solo.clickOnText("Math");
        //solo.clickInList(0,1);
        //theSessions.performItemClick(theSessions, 0, theSessions.getItemIdAtPosition(0));
        //testing the fields
//        solo.assertCurrentActivity("hmm", BidOnSessionActivity.class);
        //BidOnSessionActivity vosa = getActivity();
        TextView subjectTitle = (TextView) solo.getText(R.id.titleBody);
        assertEquals("Math", subjectTitle.getText().toString());
        TextView sessionDescription = (TextView) solo.getText(R.id.descriptionBody);
        assertEquals("Tutor for linear Algebra for all university levels",
                sessionDescription.getText().toString());
        TextView biddingStatus = (TextView) tta.findViewById(R.id.bodyStatus);
        assertTrue((biddingStatus.getText().toString() == "available") ||
                (biddingStatus.getText().toString() == "closed") ||
                (biddingStatus.getText().toString() == "pending"));

        // test if buttons are present
      //  assertNotNull(tta.findViewById(com.teamname.tutortrader.R.id.allSessionsButton));
      //  assertNotNull(tta.findViewById(com.teamname.tutortrader.R.id.editButton));
      //  assertNotNull(tta.findViewById(com.teamname.tutortrader.R.id.deleteButton));
       // assertNotNull(tta.findViewById(R.id.viewBidsButton));

    }
}

    /**
     * Testing Use Case 01.04.01 - EditSession
     * "As an owner, I want to edit a thing in my things."
     *
     *
    //EditSessionSuccess will be the case where the user clicks "save"
    public void testEditSessionSuccess () {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        Profile newProfile = new Profile("Dude","man","222");
        Session newSession = new Session ("Engls", "No courses ever",newProfile);
        ArrayList<Session> sessions = new ArrayList<Session>();

        sessions.add(newSession);

// http://blog.denevell.org/android-instrumentation-click-list.html accessed 02-2016-12
        ListView listView = (ListView)activity.findViewById(R.id.sessionList);
        listView.performItemClick(listView, 0, listView.getItemIdAtPosition(0));

        ViewOneSessionActivity vosa = (ViewOneSessionActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.editButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.editButton)).performClick();
        EditSessionActivity esa = (EditSessionActivity)getActivity();

        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.deleteButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.saveButton));

        // test to see if the fields are filled in with the previous input
        EditText oldTitle = (EditText)activity.findViewById(R.id.subjectEdit);
        EditText oldDescription = (EditText)activity.findViewById(R.id.descriptionEdit);
        assertEquals("Engls", oldTitle.getText().toString());
        assertEquals("No courses ever", oldDescription.getText().toString());

        //This edits the fields
        titleInput.setText("English");
        descriptionInput.setText("All graduate English courses or essay review help");
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.saveButton)).performClick();
        MySessionsActivity msa2 = (MySessionsActivity)getActivity();

        ArrayAdapter<Session> arrayAdapter = msa2.getAdapter();

        // To test that two sessions show up
        assertEquals(arrayAdapter.getCount(), 1);

        assertTrue("There is the English session",
                arrayAdapter.getItem(0).getTitle() == "English");
        assertTrue("The descrition is accurate",
                arrayAdapter.getItem(0).getDescription() ==
                        "All graduate English courses or essay review help");


    }

    //EditSessionCancel will be the case where the user clicks "cancel"
    public void testEditSessionCancel () {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        Profile newProfile = new Profile("Dude","man","222");
        Session newSession = new Session ("Engls", "No courses ever",newProfile);
        ArrayList<Session> sessions = new ArrayList<Session>();

        sessions.add(newSession);

        ListView listView = (ListView)activity.findViewById(R.id.sessionList);
        listView.performItemClick(listView, 0, listView.getItemIdAtPosition(0));

        ViewOneSessionActivity vosa = (ViewOneSessionActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.editButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.editButton)).performClick();
        EditSessionActivity esa = (EditSessionActivity)getActivity();

        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.deleteButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.saveButton));

        // test to see if the feilds are filled in with the previous input
        EditText oldTitle = (EditText)activity.findViewById(R.id.subjectEdit);
        EditText oldDescription = (EditText)activity.findViewById(R.id.descriptionEdit);
        assertEquals("Engls", oldTitle.getText().toString());
        assertEquals("No courses ever", oldDescription.getText().toString());

        //This edits the fields
        titleInput.setText("English");
        descriptionInput.setText("All graduate English courses or essay review help");
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.cancelButton)).performClick();
        MySessionsActivity msa2 = (MySessionsActivity)getActivity();

        ArrayAdapter<Session> arrayAdapter = msa2.getAdapter();

        // To test that the one sessions show up
        assertEquals(arrayAdapter.getCount(), 1);

        assertTrue("The title is unchanged",
                arrayAdapter.getItem(0).getTitle() == "Engls");
        assertTrue("The description is unchanged",
                arrayAdapter.getItem(0).getDescription() ==
                        "No courses ever");



    }

    /**
     * Testing Use Case 01.05.01 - DeleteSession
     * "As an owner, I want to delete a thing in my things."
     *
     * To test this we create a session, then change the activities to ge to the EditSession
     * view. The first test case tests if the delete occured when the user confirmed the delete.
     *
     * The second test tests the case when the user does not confirm the delete.
     *
    public void testDeleteSessionConfirmed () {
        MySessionsActivity msa = (MySessionsActivity)getActivity();

        Profile newProfile = new Profile("Delete","me","222");
        Session newSession = new Session ("Delete", "mememe",newProfile);
        ArrayList<Session> sessions = new ArrayList<Session>();

        sessions.add(newSession);

        ListView listView = (ListView)activity.findViewById(R.id.sessionList);
        listView.performItemClick(listView, 0, listView.getItemIdAtPosition(0));

        ViewOneSessionActivity vosa = (ViewOneSessionActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.editButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.editButton)).performClick();
        EditSessionActivity esa = (EditSessionActivity)getActivity();

        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.deleteButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.saveButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.deleteButton)).performClick();
        // to test if prompt shows up
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.confirmDelete));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.cancelDelete));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.confirmDelete)).performClick();

        MySessionsActivity msa = (MySessionsActivity)getActivity();
        ArrayAdapter<Session> arrayAdapter = msa.getAdapter();

        // To test that there are no sessions available
        assertEquals(arrayAdapter.getCount(), 0);
    }

    /** this tests to see that the session is not deleted when the user clicks cancel
    *   on the delete confirmation prompt
    *
    public void testDeleteSessionCancelled () {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
     Profile newProfile = new Profile("Dude","man","222");
     Session newSession = new Session ("Engls", "No courses ever",newProfile);
     ArrayList<Session> sessions = new ArrayList<Session>();

     sessions.add(newSession);


     ViewOneSessionActivity vosa = (ViewOneSessionActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.editButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.editButton)).performClick();
        EditSessionActivity esa = (EditSessionActivity)getActivity();

        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.deleteButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.saveButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.deleteButton)).performClick();
        // to test if prompt shows up
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.confirmDelete));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.cancelDelete));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.cancelDelete)).performClick();

        MySessionsActivity msa = (MySessionsActivity)getActivity();
        ArrayAdapter<Session> arrayAdapter = msa.getAdapter();

        // To test that there are no sessions available
        assertEquals(arrayAdapter.getCount(), 1);
        assertTrue(arrayAdapter.getItem(0).getTitle() == "Delete Me");
        assertTrue(arrayAdapter.getItem(0).getDescription() ==
                        "Delete this session");


    }
}
*/