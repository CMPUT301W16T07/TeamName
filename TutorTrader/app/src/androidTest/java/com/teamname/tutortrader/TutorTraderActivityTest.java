package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by taylorarnett on 2016-02-10.
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
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        int oldLength = tta.getAdapter().getCount();
        createSession("Math", "Tutor for linear Algebra for all university levels");
        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(oldLength +1, arrayAdapter.getCount());

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
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        int oldLength = tta.getAdapter().getCount();
        createSession("Math", null);
        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(oldLength, arrayAdapter.getCount());

        assertFalse("Did you add a Session object?",
                arrayAdapter.getItem(arrayAdapter.getCount() - 1) instanceof Session);

    }

    /**
     * Testing UseCase 01.02.01 - ViewSessions
     * "As an owner, I want to view a list of all my sessions, and their descriptions and statuses."
     *
     */
     
     


    /**
    *	US 04.01.01 - As a borrower, I want to specify a set of keywords, and search for all things not currently
    *                 borrowed whose description contains all keywords. 
    *	Assumes that the search bar exists in the main Activity and does not have its own View.
    *	Assumes the search updates the existing Array adapter with the new search information
    *
    */
    private void createSearch(String searchText){
    	assertNotNull(activity.findViewById(com.teamneam.tutortrader.R.id.search));
    	searchInput.setText(searchText);
	    ((Button) activity.findViewById(com.teamneam.tutortrader.R.id.search)).performClick();
    }

    public void testSearchOneWord(){
	TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
	createSession("Math", "Tutor for highschool math classes.");
	createSession("Math", "Tutor for university math classes.");
	createSearch("highschool")

	ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
	assertEquals(1, arrayAdapter.getCount()); 
	Session session = arrayAdapter.getItem(arrayAdapter.getCount()-1);
    	assertEquals("Should be math title", session.getTitle(), "Math");
	assertEquals("Should be the description for highschool math session", session.getDescription(), "Tutor for highschool math classes.");	
    }

    /*
    * should only get the Session with both 'highschool' and 'math' in the description
     */
    public void testSearchTwoWords(){
    	TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
    	createSession("Math", "Tutor for highschool math classes.");
    	createSession("Math", "Tutor for university math classes.");
    	createSession("Biology", "Tutor for biology 101, 102");
    	
    	createSearch("highschool math");
    
    	ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
    	assertEquals(1, arrayAdapter.getCount());
    	
    	Session session = arrayAdapter.getItem(arrayAdapter.getCount()-1);

    	assertEquals("Should be math, highschool math", session.getTitle(), "Math");
        assertEquals("Should be math, highschool math",session.getDescription(), "Tutor for highschool math classes.");

    }

    /**
    *	check that empty list is empty.
    *	
    *
    */
    public void testSearchEmpty(){
    	TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
    	createSession("Math", "Tutor for highschool math classes.");
    	
    	createSearch("nothing");
    	
    	ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
    	assertEquals(0, arrayAdapter.getCount()); 
    	
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

    /*
    *   US - US 07.01.01 - RETURNING
    *   As an owner, I want to set a borrowed thing to be available when it is returned.
    *   Creates a session. goes yo session info. Clicks edit. And click the 'repost' button
     */

    public void testReturn(){
        TutorTraderActivity tta = (TutorTraderActivity)getActivity();
        int oldLength = tta.getAdapter().getCount();
        createSession("Math", "Tutor for highschool math classes.");

        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(oldLength + 1, arrayAdapter.getCount());

        arrayAdapter.getView(oldLength - 1, null ,null).performClick();
        activity.findViewById(R.id.edit).performClick();
        activity.findViewById(R.id.repost).performClick();

        assertEquals("check status is back to available", arrayAdapter.getItem(oldLength -1).getStauts, "available");


    }

         
}
