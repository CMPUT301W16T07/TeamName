package com.teamname.tutortrader;

import android.print.PageRange;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Stores all information about a user (their name, number,
 * email, etc).
 */
public class Profile {

    private UUID ProfileID;
    private String name = null;
    private String phone;
    private String email;
    private ArrayList<Double> tutorRatings; // an array of all tutor ratings
    private ArrayList<Double> studentRatings; // an array of all student ratings
    private boolean defaultUser = Boolean.TRUE;

    //private static final Profile instance = new Profile();


    public boolean isDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(boolean defaultUser) {
        this.defaultUser = defaultUser;
    }

    public Profile(String name, String email, String phone){
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

    /**
     * Returns the average of all tutor ratings for this user - i.e.,
     * their actual tutor rating.
     */
    public Double getTutorRating() {
        return 1.0;
    }

    /**
     * Returns the average of all student ratings for this user - i.e.,
     * their actual student rating.
     */
    public Double getStudentRating() {
        return 1.0;
    }
}
