package com.teamname.tutortrader;

import java.util.UUID;

/**
 * Created by taylorarnett on 2016-03-01.
 */
public class Profile {

    private UUID ProfileID;
    private String name;
    private String phone;
    private String email;

    public void Profile(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
        //set ID
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
}
