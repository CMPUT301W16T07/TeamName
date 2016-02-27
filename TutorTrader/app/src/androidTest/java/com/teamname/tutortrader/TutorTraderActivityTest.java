package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by taylorarnett on 2016-02-10.
 *
 * For tests dealing with sessions.
 */
public class TutorTraderActivityTest extends ActivityInstrumentationTestCase2 {

    Instrumentation instrumentation;
    Activity activity;
    EditText titleInput;
    EditText descriptionInput;
    EditText searchInput;

    public TutorTraderActivityTest(Class activityClass) {
        super(activityClass);
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
     * @param title a string input of the subject of the tutoring session
     * @param description a string input of the description of the tutoring session.
     */
    private void createSession(String title, String description) {
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.save));
        titleInput.setText(title);
        descriptionInput.setText(description);
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.save)).performClick();
    }

    /** USECASE 1 = AddSession
     * Testing if a session is added after all text fields are inputted correctly.
     * This Tests the UI
     */
    public void testAddSessionValid() {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        int oldLength = msa.getAdapter().getCount();
        createSession("Math", "Tutor for linear Algebra for all university levels");
        ArrayAdapter<Session> arrayAdapter = msa.getAdapter();
        assertEquals(oldLength + 1, arrayAdapter.getCount());

        assertTrue("Did you add a Session object?",
                arrayAdapter.getItem(arrayAdapter.getCount() - 1) instanceof Session);

        Session session = arrayAdapter.getItem(arrayAdapter.getCount() - 1);
        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");
    }

    /** USECASE 1 = AddSession
     * This tests use case 1 but in the case where the input fields are incomplete
     * This tests the UI
     */
    public void testAddSessionIncomplete() {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        int oldLength = msa.getAdapter().getCount();
        createSession("Math", null);
        ArrayAdapter<Session> arrayAdapter = msa.getAdapter();
        assertEquals(oldLength, arrayAdapter.getCount());

        assertFalse("Did you add a Session object?",
                arrayAdapter.getItem(arrayAdapter.getCount() - 1) instanceof Session);

    }

    /**
     * Testing UseCase 01.02.01 - ViewSessions
     * "As an owner, I want to view a list of all my sessions, and their descriptions and statuses."
     *
     * To test, we create 2 new sessions and then we leave the MySessions view, and return
     * to the view to see if the sessions persist.
     */
    public void testViewSessions () {
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.MySessionsButton));
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.mySessionsButton)).performClick();
        MySessionsActivity msa = (MySessionsActivity)getActivity();

        createSession("Math", "Tutor for linear Algebra for all university levels");
        createSession("Stats", "Tutor for Stats 252 and 141");
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.currentBidsButton));
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.availableSessionsButton)).performClick();
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.mySessionsButton)).performClick();
        MySessionsActivity msa = (MySessionsActivity)getActivity();

        ArrayAdapter<Session> arrayAdapter = msa.getAdapter();

        // To test that two sessions show up
        assertEquals(arrayAdapter.getCount(), 2);

        assertTrue("There is the math session",
                arrayAdapter.getItem(0).getTitle() == "Math");
        assertTrue("There is the stats session",
                arrayAdapter.getItem(0).getTitle() == "Stats");
    }

    /**
     * Testing UseCase 01.04.01 - ViewOneSession
     * "As an owner, I want to view one of my things, its description and status."
     *
     * We will perform a click on list entry to bring us to the ViewOneSession view.
     * From here we test to see that all the buttons are present, and all the TextViews are
     * accurate
     */
    public void testViewOneSession() {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.MySessionsButton));

        createSession("Math", "Tutor for linear Algebra for all university levels");

        // http://blog.denevell.org/android-instrumentation-click-list.html accessed 02-2016-12
        ListView listView = (ListView)activity.findViewById(com.teamname.tutortrader.R.id.MySessionsList);
        listView.performItemClick(listView, 0, listView.getItemIdAtPosition(0));

        //testing the fields
        ViewOneSessionActivity vosa = (ViewOneSessionActivity)getActivity();
        (TextView) subjectTitle = (TextView)activity.findViewById(com.teamname.tutortrader.R.id.subjectTitle);
        assertEquals("Math", subjectTitle.getText().toString());
        (TextView) sessionDescription = (TextView)activity.findViewById(com.teamname.tutortrader.R.id.sessionDescription);
        assertEquals("Tutor for linear Algebra for all university levels",
                sessionDescription.getText().toString());
        (TextView) biddingStatus = (TextView)activity.findViewById(com.teamname.tutortrader.R.id.biddingStatus);
        assertTrue((sessionDescription.getText().toString() == "Available") ||
                (sessionDescription.getText().toString() == "Closed") ||
                (sessionDescription.getText().toString() == "Pending"));

        // test if buttons are present
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.allSessionsButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.editButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.deleteButton));

    }

    /**
     * Testing Use Case 01.04.01 - EditSession
     * "As an owner, I want to edit a thing in my things."
     *
     */
    //EditSessionSuccess will be the case where the user clicks "save"
    public void testEditSessionSuccess () {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        createSession("Engls", "No courses ever");

        ViewOneSessionActivity vosa = (ViewOneSessionActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.editButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.editButton)).performClick();
        EditSessionActivity esa = (EditSessionActivity)getActivity();

        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.deleteButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.saveButton));

        // test to see if the fields are filled in with the previous input
        (EditText) oldTitle = (EditText)activity.findViewById(com.teamname.tutortrader.R.id.subjectTitle);
        (EditText) oldDescription = (EditText)activity.findViewById(com.teamname.tutortrader.R.id.sessionDescription);
        assertEquals("Engls", oldTitle.getText().toString());
        assertEquals("No courses ever", oldDescription.getText().toString());

        //This edits the fields
        titleInput.setText("English");
        descriptionInput.setText("All graduate English courses or essay review help");
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.saveButton)).performClick();
        MySessionsActivity msa = (MySessionsActivity)getActivity();

        ArrayAdapter<Session> arrayAdapter = msa.getAdapter();

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
        createSession("Engls", "No courses ever");

        ViewOneSessionActivity vosa = (ViewOneSessionActivity)getActivity();
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.editButton));
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.editButton)).performClick();
        EditSessionActivity esa = (EditSessionActivity)getActivity();

        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.deleteButton));
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.saveButton));

        // test to see if the feilds are filled in with the previous input
        (EditText) oldTitle = (EditText)activity.findViewById(com.teamname.tutortrader.R.id.subjectTitle);
        (EditText) oldDescription = (EditText)activity.findViewById(com.teamname.tutortrader.R.id.sessionDescription);
        assertEquals("Engls", oldTitle.getText().toString());
        assertEquals("No courses ever", oldDescription.getText().toString());

        //This edits the fields
        titleInput.setText("English");
        descriptionInput.setText("All graduate English courses or essay review help");
        ((Button)activity.findViewById(com.teamname.tutortrader.R.id.cancelButton)).performClick();
        MySessionsActivity msa = (MySessionsActivity)getActivity();

        ArrayAdapter<Session> arrayAdapter = msa.getAdapter();

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
     */
    public void testDeleteSessionConfirmed () {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        createSession("Delete Me", "Delete this session");

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
    */
    public void testDeleteSessionCancelled () {
        MySessionsActivity msa = (MySessionsActivity)getActivity();
        createSession("Delete Me", "Delete this session");

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
