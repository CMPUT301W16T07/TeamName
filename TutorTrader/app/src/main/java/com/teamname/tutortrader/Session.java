package com.teamname.tutortrader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Holds information for individual tutor sessions.
 *
 * Session object class.
 *
 */
public class Session {
    private String title;
    private String description;
    private String status;
    private UUID tutorID;
    private UUID sessionID;
    private ArrayList<Bid> bids;
    protected transient Bitmap thumbnail;
    protected String thumbnailBase64;
    private LatLng location;


    public Session(String title, String description, UUID tutorID, Bitmap thumbnail,LatLng location) {
        this.title = title;
        this.description = description;
        this.status = "available";
        this.tutorID = tutorID;
        this.sessionID = UUID.randomUUID();
        this.bids = new ArrayList<Bid>();
        this.thumbnail = thumbnail;
        this.location = location;

    }

    public Session(String title, String description, UUID tutorID, Bitmap thumbnail) {
        this.title = title;
        this.description = description;
        this.status = "available";
        this.tutorID = tutorID;
        this.sessionID = UUID.randomUUID();
        this.bids = new ArrayList<Bid>();
        this.thumbnail = thumbnail;

    }

    public Session(String title, String description, UUID tutorID) {

        this.title = title;
        this.description = description;
        this.status = "available";
        this.tutorID = tutorID;
        this.sessionID = UUID.randomUUID();
        this.bids = new ArrayList<Bid>();
        this.thumbnail = null;
    }

    public UUID getTutorID() {
        return tutorID;
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

    public int getBidsCount () {
        return bids.size();
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

    public void setStatus(String status) {
        if ((status.equals("available"))|| (status.equals("bidded"))|| (status.equals("booked"))) {
            this.status = status;
        }
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
    public void declineAllBids () {
        for (int i=0; i< bids.size(); i++) {
            bids.get(i).setStatus("declined");
        }
    }
    public void deleteAllBids () {
        bids.clear();
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nDescription: " + description ;
    }

    public void addThumbnail(Bitmap newThumbnail){
        if (newThumbnail != null) {
            thumbnail = newThumbnail;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newThumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] b = byteArrayOutputStream.toByteArray();
            thumbnailBase64 = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    public Bitmap getThumbnail(){
        if (thumbnail == null && thumbnailBase64 != null){
            byte[] decodeString = Base64.decode(thumbnailBase64, Base64.DEFAULT);
            thumbnail = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        }
        return thumbnail;
    }
}
