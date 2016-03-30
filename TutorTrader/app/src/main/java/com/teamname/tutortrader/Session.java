package com.teamname.tutortrader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
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
    protected transient Bitmap thumbnail;
    protected String thumbnailBase64;

    public Session(String title, String description, Profile tutor, Bitmap thumbnail) {
        this.title = title;
        this.description = description;
        this.status = "available";
        this.tutor = tutor;
        this.sessionID = UUID.randomUUID();
        this.bids = new ArrayList<Bid>();
        this.thumbnail = thumbnail;

    }
    public Session(String title, String description, Profile tutor) {
        this.title = title;
        this.description = description;
        this.status = "available";
        this.tutor = tutor;
        this.sessionID = UUID.randomUUID();
        this.bids = new ArrayList<Bid>();
        this.thumbnail = null;
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

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
