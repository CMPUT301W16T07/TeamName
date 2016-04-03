package com.teamname.tutortrader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.robotium.solo.Solo;

/**
 * Created by abrosda on 4/3/16.
 */
public class MyProfilesActivityTest extends ActivityInstrumentationTestCase2 {

    public MyProfilesActivityTest(Class activityClass) {
        super(activityClass);
    }

    private Solo solo;
    Profile profile;
    Bitmap.Config conf;
    Bitmap bm1;
    Session session;
    Activity activity;


    public void setUp() throws Exception {
        super.setUp();
        //setUp() is run before a test case is started.
        //This is where the solo object is create   d.
        solo = new Solo(getInstrumentation());
        activity = getActivity();
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


    public void addSessionTest () {
        try {
            setUp();
        }catch(Exception e){
            e.printStackTrace();
        }
        Button addNewSession = (Button) activity.findViewById();

    }
    private void makeSession(String text) {
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.body));
        textInput.setText(text);
        ((Button) activity.findViewById(ca.ualberta.cs.lonelytwitter.R.id.save)).performClick();
    }
}
