package com.teamname.tutortrader;

import java.util.UUID;

/**
 *
 * Stores information about individual bids
 * which must then be added to a session to
 * be meaningful.
 */
public class Bid {

    private UUID bidID; // unique identifying ID
    private UUID bidder; // a user ID
    UUID sessionID; // a session ID
    private Float amount; // in dollars per hour
    private String status; // pending, accepted, declined, etc.

    // initialize Bids by passing a session ID, bidder ID, and amount
    public Bid(UUID session, UUID bidder, Float amount) {

        this.bidID = UUID.randomUUID();
        this.bidder = bidder;
        this.sessionID = session;
        this.amount = amount;
        this.status = "Pending";
    }

    public UUID getBidder() {
        return bidder;
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public Float getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    // set status if status is valid

    public void setStatus(String status) {
        if ((status.equals("pending"))|| (status.equals("accepted")) || (status.equals("declined"))) {
            this.status = status;
        }
    }

    // get Session from session ID associated with bid
    public Session getBidSession() {
        return MethodsController.getSession(this.sessionID); // from MethodsController
    }

    @Override
    public String toString() {
        return "Session: " + "TEST SESSION" + " || Amount: " + amount + " || Status: " + status;
    }
}
