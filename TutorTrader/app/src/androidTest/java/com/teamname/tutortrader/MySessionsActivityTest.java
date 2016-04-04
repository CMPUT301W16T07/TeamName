package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * This is a comprehensive test of the ui and the
 * classes/methods involved in this activity
 * For this test you must first run the app regularly to
 * create a unique profile on the emulator or device
 */
public class MySessionsActivityTest extends ActivityInstrumentationTestCase2 {

    public MySessionsActivityTest() {
        super(MySessionsActivity.class);
    }
    private Solo solo;
    Profile profile;
    Bitmap.Config conf;
    Instrumentation instrumentation;
    Bitmap bm1;
    Session session;
    Activity activity;
    EditText text;


    public void setUp() throws Exception {
        super.setUp();
        //setUp() is run before a test case is started.
        //This is where the solo object is create   d.
        solo = new Solo(getInstrumentation());
        activity = getActivity();
        instrumentation = getInstrumentation();
         profile = new Profile("TESTER", "test@test.test", "780-666-6666");
         conf = Bitmap.Config.ARGB_8888;
         bm1 = Bitmap.createBitmap(1, 2, conf);
         session = new Session("Math", "Tutor for linear Algebra for all university levels", profile.getProfileID(), bm1);
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
        super.tearDown();
    }

    /**
     * You must have a profile already built to run these tests.
     */
    public void testaddSession () {
        solo.clickOnButton(4);

        solo.typeText(0, "TITLE");
        solo.typeText(1, "Description");

        solo.clickOnButton("Cancel");

        solo.clickOnMenuItem("My Sessions");
        solo.clickOnButton("Add");

        solo.typeText(0, "TITLE");
        solo.typeText(1, "Description");

        solo.clickOnButton("Save");
        solo.clickOnMenuItem("My Sessions");

        assertTrue(solo.searchText("TITLE"));
        assertTrue(solo.searchText("Description"));

        solo.clickOnText("TITLE");
        solo.assertCurrentActivity("We switched sessions", ViewOneSessionActivity.class);

        solo.clickOnButton("View");
        solo.assertCurrentActivity("We are viewing bids", ViewBidsActivity.class);
        solo.clickOnButton(0);
        solo.clickOnButton("Edit");

        solo.assertCurrentActivity("Editing session", EditSessionActivity.class);
        solo.clickOnButton("Cancel");

        solo.assertCurrentActivity("Cancelled out", MySessionsActivity.class);
        solo.clickOnText("TITLE");
        solo.clickOnButton("Edit");

        solo.clearEditText(1);
        solo.typeText(1, "New Description");
        solo.clickOnButton("Save");

        solo.clickOnMenuItem("My Sessions");
        assertTrue(solo.searchText("New Description"));
        solo.clickOnText("TITLE");
        solo.clickOnButton("Delete");
        solo.sleep(2000);
        solo.clickOnText("Yes");
        assertFalse(solo.searchText("TITLE"));


    }




}
