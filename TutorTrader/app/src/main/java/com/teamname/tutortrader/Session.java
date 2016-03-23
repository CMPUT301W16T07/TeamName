package com.teamname.tutortrader;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by taylorarnett on 2016-03-01.
 *
 * Holds information for individual tutor sessions.
 */

/**
 * Session object class
 *
 */
public class Session {
    private String title;
    private String description;
    protected Profile tutor;
    private String status;
    private UUID sessionID;
    private ArrayList<Bid> bids;

    public Session(String title, String description, Profile tutor) {
        this.title = title;
        this.description = description;
        this.status = "available";
        this.tutor = tutor;
        this.sessionID = UUID.randomUUID();
        this.bids = new ArrayList<Bid>();

    }

    /**
     * When creates a new bid the bid is passed into addBid for the specific session.
     * @param bid This is a bid object that will be added to the array of bids for a given session.
     */
    public void addBid(Bid bid) {
        bids.add(bid);
    }

    /**
     * getBids will return a list of bids that a specific session has.
     *
     * @return will return an arraylist of bids.
     */
    public ArrayList<Bid> getBids () {
        return bids;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    //fixed issue: == used .equals() instead
    public void setStatus(String status) {//} throws InvalidStatusException {
        if ((status.equals("available"))|| (status.equals("bidded"))|| (status.equals("booked"))) {
            this.status = status;
        } else {
            //throw new InvalidStatusException();
        }
    }

    public void declineAllBids () {
        for (int i=0; i< bids.size(); i++) {
            bids.get(i).setStatus("declined");
        }
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nDescription: " + description ;
    }
}
