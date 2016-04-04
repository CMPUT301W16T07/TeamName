package com.teamname.tutortrader;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 *
 * Tests the MyProfileActivity Class.
 *
 * Created by abrosda on 4/3/16.
 */
public class MyProfilesActivityTest extends ActivityInstrumentationTestCase2 {

    public MyProfilesActivityTest(Class activityClass) {
        super(activityClass);
    }

    private Solo solo;

    public void setUp() throws Exception {
        super.setUp();
        //setUp() is run before a test case is started.
        //This is where the solo object is create   d.
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
        super.tearDown();
    }


    public void addSessionTest () {

    }
    private void makeSession(String text) {

    }
}
