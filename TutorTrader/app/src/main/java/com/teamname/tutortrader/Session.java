package com.teamname.tutortrader;

import java.util.ArrayList;

/**
 * Created by taylorarnett on 2016-03-01.
 */

/**
 * Session object class
 */
public class Session {
    private String title;
    private String description;
    private Profile tutor;
    private String status;
    private Integer sessionID;
    private ArrayList<Bid> bids;

    public Session(String title, String description, Profile tutor) {
        this.title = title;
        this.description = description;
        this.status = "available";
        this.tutor = tutor;
        // TODO: figure out android ID generator. Also needed in Profile.class
        this.sessionID = null;
        this.bids = new ArrayList<Bid>();

    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public ArrayList<Bid> getBids () {
        return bids;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setStatus(String status) throws InvalidStatusException {
        if ((status == "available")|| (status == "bidded") || (status == "booked")) {
            this.status = status;
        } else {
            throw new InvalidStatusException();
        }
    }

    @Override
    public String toString() {
        return "Title: " + title + " Description: " + description;
    }
}
