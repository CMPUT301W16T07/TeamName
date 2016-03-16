package com.teamname.tutortrader;

import java.util.UUID;

/**
 * Created by taylorarnett on 2016-03-01.
 *
 * Stores information about individual bids
 * which must then be added to a session to
 * be meaningful.
 */
public class Bid {

    private UUID bidID; // unique identifying ID
    private UUID bidder; // a user ID
    private UUID sessionID; // a session ID
    private String sessionTitle; // the title of the session
    private Float amount; // in dollars per hour
    private String status; // pending, accepted, declined, etc.

    // initialize Bids by passing a session ID, bidder ID, and amount
    public Bid(UUID session, String title, UUID bidder, Float amount) {
        this.bidID = UUID.randomUUID();
        this.bidder = bidder;
        this.sessionID = session;
        this.sessionTitle = title;
        this.amount = amount;
        this.status = "pending";
    }

    public UUID getBidID() {
        return bidID;
    }

    public UUID getBidder() {
        return bidder;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    // set status if status is valid
    public void setStatus(String status) {//throws InvalidStatusException {
        if ((status == "pending")|| (status == "accepted") || (status == "declined")) {
            this.status = status;
        } else {
           // throw new InvalidStatusException();
        }
    }

    @Override
    public String toString() {
        return "Session: " + sessionTitle + " || Amount: " + amount + " || Status: " + status;
    }
}
