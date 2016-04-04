package com.teamname.tutortrader;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


/**
 * This class tests the current bids activity. To use this test
 * you have to already have a profile made! This tests UI and methods
 */
public class CurrentBidsActivityTest extends ActivityInstrumentationTestCase2{
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
    public CurrentBidsActivityTest() {
        super(CurrentBidsActivity.class);
    }

    public void testCurrentBids(){
        solo.assertCurrentActivity("right activity", CurrentBidsActivity.class);

        Profile profile = new Profile("Test tutor", "test@test.test", "780-666-6666");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bm1 = Bitmap.createBitmap(1, 2, conf);
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels", profile.getProfileID(), bm1);

        ElasticSearchController.AddProfileTask profileTask = new ElasticSearchController.AddProfileTask();
        profileTask.execute(profile);
        ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
        addSessionTask.execute(session);

        solo.clickOnMenuItem("Available");
        solo.clickOnText("Math");
        solo.typeText(0, "2");
        solo.clickOnButton(2);

        solo.clickOnMenuItem("Current");

        solo.clickOnButton(4);
        solo.sleep(4000);
        Profile owner = MethodsController.getProfile(session.getTutorID());
        ElasticSearchController.RemoveProfileTask removeProfileTask = new ElasticSearchController.RemoveProfileTask();
        removeProfileTask.execute(owner.getProfileID());

        ElasticSearchController.RemoveSessionTask removeSessionTask = new ElasticSearchController.RemoveSessionTask();
        removeSessionTask.execute(session.getSessionID());


    }
}
