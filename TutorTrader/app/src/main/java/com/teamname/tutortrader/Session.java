package com.teamname.tutortrader;

import java.util.ArrayList;

/**
 * Created by taylorarnett on 2016-03-01.
 */

/**
 * Session object class
 */
public class Session {
    public String title;
    public String description;
    public Profile tutor;
    public String status;
    public Integer sessionID;
    public ArrayList<Bid> bids;

    public Session(String title, String description, Profile tutor) {
        this.title = title;
        this.description = description;
        this.status = "available";

    }


}
