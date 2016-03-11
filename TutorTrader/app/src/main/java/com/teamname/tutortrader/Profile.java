package com.teamname.tutortrader;

import android.print.PageRange;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by taylorarnett on 2016-03-01.
 */


public class Profile {

    private UUID ProfileID;
    private String name;
    private String phone;
    private String email;

   // private static final Profile instance = new Profile();


    public Profile(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
        //set
        this.ProfileID = UUID.randomUUID();
    }



    public UUID getProfileID() {
        return ProfileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns an array list of all current bids that this user
     * has placed on other user's sessions.
     */
    public ArrayList<Bid> getCurrentBids() {
        return new ArrayList<>();
    }
}
