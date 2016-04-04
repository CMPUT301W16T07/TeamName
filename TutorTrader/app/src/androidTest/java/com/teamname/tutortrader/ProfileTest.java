package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * US 03.02.01
 * As a user, I want to edit the contact information in my profile.
 *
 * For profile tests.
 */

public class ProfileTest extends ActivityInstrumentationTestCase2 {

    Instrumentation instrumentation;
    Activity activity;
    EditText username;
    EditText email;
    EditText phone;

    public ProfileTest() {
        super(AvailableSessionsActivity.class);
    }


    public void testEditEmail() {
        Profile user = new Profile("john","123456","poop");
        user.setEmail("randomemail@gmail.ca");
        assertEquals(user.getEmail(), "randomemail@gmail.ca");
    }

    public void testEditPhone() {
        Profile user = new Profile("john","this should be changed","randomemail@email.email");
        user.setPhone("7804737373");
        assertEquals(user.getPhone(),"7804737373");
    }
    public void testEditName(){
        Profile user = new Profile("john","this should not be changed","randomemail@email.email");
        user.setName("worked");
        assertEquals(user.getName(), "worked");
    }
//TODO:HELP
    @UiThreadTest
    public void testUnique(){
        activity = getActivity();
        activity.setContentView(R.layout.create_profile);
        CreateProfileActivity creator = new CreateProfileActivity();

        Profile user = new Profile("john","ay", "baanana");
        Profile user2 = new Profile("john","lol","bananan1");

        ElasticSearchController.AddProfileTask profileTask = new ElasticSearchController.AddProfileTask();
        profileTask.execute(user);

        EditText newUsername = (EditText) activity.findViewById(R.id.editUsername);
        EditText newEmail = (EditText) activity.findViewById(R.id.editEmail);
        EditText newPhone = (EditText) activity.findViewById(R.id.editPhone);
        newUsername.setText("john");
        newEmail.setText("test");
        newPhone.setText("123456");

        final Button saveButton = (Button) activity.findViewById(R.id.saveButton);
        saveButton.performClick();

        ArrayList<Profile> profiles = new ArrayList<>();
        ElasticSearchController.GetProfileTask getProfileTask = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute("email", "test");
        try {
            profiles = getProfileTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        assertEquals(0, profiles.size());

    }
}