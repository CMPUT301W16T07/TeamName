package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import javax.net.ssl.SSLSessionBindingListener;

/**
 * malba @12/2/2016
 * For bidding use cases.
 */

public class BiddingTest extends ActivityInstrumentationTestCase2 {

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