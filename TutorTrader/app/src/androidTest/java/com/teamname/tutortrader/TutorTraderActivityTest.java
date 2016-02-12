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

        Session session = arrayAdapter.getItem(arrayAdapter.getCount()-1);
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
	Session session = arrayAdapter.getItem(arrayAdapter.getCount() - 1);
    	assertEquals("Should be math title", session.getTitle(), "Math");
	assertEquals("Should be the description for highschool math session", session.getDescription(), "Tutor for highschool math classes.");	
    }

    
    /**
    *	how do we want the search to deal with multiple words?
    *	do we look for partial matches or ignore those?
    *	do we look for exact matches or just descriptions with two matching words?
    * 	test assumes search words individualy.
    **/
    public void testSearchTwoWords(){
    	TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
    	createSession("Math", "Tutor for highschool classes.");
    	createSession("Math", "Tutor for university math classes.");
    	createSession("Biology", "Tutor for biology 101, 102");
    	
    	createSearch("highschool math");
    
    	ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
    	assertEquals(2, arrayAdapter.getCount()); 
    	
    	Session session = arrayAdapter.getItem(arrayAdapter.getCount()-1);
    	assertNotEquals("Should not be Bio title", session.getTitle(), "Biology");
    	session = arrayAdapter.getItem(arrayAdapter.getCount()-2);
    	assertNotEquals("Should not be Bio title", session.getTitle(), "Biology")
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


    /** USECASE 5 - BidSession
     *  bidSession(bid) fills in the bid amount
     *  clicks the 'bid' button for the activity:
     * @param title a string input of the bid amount.
     */
    private void bidSession(String bid) {
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.bid));
        bidInput.setText(bid);
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.bid)).performClick();
    }

    /** USECASE 5 - BidSession
     *  acceptBid() accepts some bid
     *  clicks the 'accept' button for the activity:
     */
    private void acceptBid() {
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.acceptBid));
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.acceptBid)).performClick();
    }

    /** USECASE 5 - BidSession
     *  declineBid() declines some bid
     *  clicks the 'decline' button for the activity:
     */
    private void declineBid() {
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.declineBid));
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.declineBid)).performClick();
    }

    /** USECASE 05.01.01 = BidForSession
     * Testing if a session's status is changed to pending after a valid bid is added.
     * Test if bid has been added to bids array.
     */
    public void testBidForSessionValid() {

        Session session = new Session("Math", "Tutor for linear Algebra for all university levels");
        Bid bid = new Bid();
        bid.username = "test"; // valid username
        bid.amount = 12.13; // valid amount

        bidSession(bid.amount);

        assertEquals("The session now has a status of pending.", session.getStatus(), "pending");
        assertEquals("The session's bid count has been incremented.", session.getBidsCount(), 1);
        assertTrue("The session's bid array has a new bid.", session.getBids().contains(bid));
    }

    /** USECASE 05.01.01 = BidForSession
     * Testing if a session's status is changed to pending after an invalid bid is added.
     * This Tests the UI.
     */
    public void testBidForSessionInvalid() {

        Session session =  new Session("Math", "Tutor for linear Algebra for all university levels");
        Bid bid = new Bid();
        bid.username = "test"; // valid username
        bid.amount = "this is not an amount"; // invalid amount

        bidSession(bid.amount);

        assertEquals("The session still has a status of available.", session.getStatus(), "available");
        assertEquals("The session's bid count has not been incremented.", session.getBidsCount(), 0);
        assertFalse("The session's bid array is empty.", session.getBids().contains(bid));
    }

    /** USECASE 05.01.02 = ViewPendingBids
     * Testing if a student can view their pending bids.
     * This tests the UI.
     */
    public void testViewPendingBids() {
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        int oldLength = tta.getAdapter().getCount();
        createSession("Math", "Tutor for linear Algebra for all university levels");
        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(oldLength, arrayAdapter.getCount()); // still no pending bids...

        Session session = arrayAdapter.getItem(arrayAdapter.getCount() - 1);
        Bid bid = new Bid();
        bid.username = "test"; // valid username
        bid.amount = 12.13; // valid amount

        bidSession(bid.amount);
        assertEquals(oldLength + 1, arrayAdapter.getCount()); // new bid in array
    }

    /** USECASE 05.01.04 = ViewSessionsWithBids
     * Testing if a tutor can view their sessions with pending bids.
     * This tests the UI.
     */
    public void testViewSessionsWithBids() {
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        int oldLength = tta.getAdapter().getCount();
        createSession("Math", "Tutor for linear Algebra for all university levels");
        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(oldLength, arrayAdapter.getCount()); // still no pending bids...

        Session session = arrayAdapter.getItem(arrayAdapter.getCount() - 1);
        Bid bid = new Bid();
        bid.username = "test"; // valid username
        bid.amount = 12.13; // valid amount

        bidSession(bid.amount);
        assertEquals(oldLength + 1, arrayAdapter.getCount()); // new bid in array
    }

    /** USECASE 05.01.06 = AcceptBidOnSession
     * Testing if a tutor can accept a bid on a session.
     * This tests the UI.
     */
    public void testAcceptBidOnSession() {

        Session session = new Session("Math", "Tutor for linear Algebra for all university levels");
        Bid bid = new Bid();
        bid.username = "test"; // valid username
        bid.amount = 12.13; // valid amount

        bidSession(bid.amount);

        assertEquals("The session now has a status of pending.", session.getStatus(), "pending");
        assertEquals("The session's bid count has been incremented.", session.getBidsCount(), 1);
        assertTrue("The session's bid array has a new bid.", session.getBids().contains(bid));

        acceptBid();

        assertEquals("The session now has a status of accepted.", session.getStatus(), "accepted");
    }

    /** USECASE 05.01.07 = DeclineBidOnSession
     * Testing if a tutor can decline a bid on a session.
     * This tests the UI.
     */
    public void testDeclineBidOnSession() {

        Session session = new Session("Math", "Tutor for linear Algebra for all university levels");
        Bid bid = new Bid();
        bid.username = "test"; // valid username
        bid.amount = 12.13; // valid amount

        bidSession(bid.amount);

        assertEquals("The session now has a status of pending.", session.getStatus(), "pending");
        assertEquals("The session's bid count has been incremented.", session.getBidsCount(), 1);
        assertTrue("The session's bid array has a new bid.", session.getBids().contains(bid));

        declineBid();

        assertEquals("The session now has a status of available.", session.getStatus(), "available");
        assertEquals("The session's bid count has been decremented.", session.getBidsCount(), 0);
        assertFalse("The session's bid array lost the bid.", session.getBids().contains(bid));
    }
}
