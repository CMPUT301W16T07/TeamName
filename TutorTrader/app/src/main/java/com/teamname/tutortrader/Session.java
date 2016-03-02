package com.teamname.tutortrader;

import java.util.ArrayList;

/**
 * Created by taylorarnett on 2016-03-01.
 */
public class Session {
    public String title;
    public String description;
    public Tutor tutor;
    public String status;
    public Integer sessionID;
    public ArrayList<Bid> bids;

    public Session(String title, String description, Tutor tutor) {
        this.title = title;
        this.description = description;
        this.status = "available";

    }

}
