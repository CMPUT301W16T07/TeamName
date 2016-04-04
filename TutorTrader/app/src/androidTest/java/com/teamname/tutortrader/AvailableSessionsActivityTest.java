package com.teamname.tutortrader;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;


/**
 * This will test both the API and the code to see if we can add sessions, view sessions
 * search sessions and delete sessionss
 *
 */
public class AvailableSessionsActivityTest extends ActivityInstrumentationTestCase2 {


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
    public AvailableSessionsActivityTest() {
        super(AvailableSessionsActivity.class);
    }

    public void testTest(){
        Profile profile = new Profile("Test tutor", "test@test.test", "780-666-6666");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bm1 = Bitmap.createBitmap(1, 2, conf);
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels", profile.getProfileID(), bm1);

        Profile owner = MethodsController.getProfile(session.getTutorID());
        ElasticSearchController.RemoveProfileTask removeProfileTask = new ElasticSearchController.RemoveProfileTask();
        removeProfileTask.execute(owner.getProfileID());

        ElasticSearchController.RemoveSessionTask removeSessionTask = new ElasticSearchController.RemoveSessionTask();
        removeSessionTask.execute(session.getSessionID());

        solo.clickOnMenuItem("Profile");
        solo.sleep(2000);
        solo.clickOnMenuItem("Available");
    }

    public void testViewAvailable() {
        Profile profile = new Profile("Test tutor", "test@test.test", "780-666-6666");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bm1 = Bitmap.createBitmap(1, 2, conf);
        Session session = new Session("TESTING", "Tutor for linear Algebra for all university levels", profile.getProfileID(), bm1);

        ElasticSearchController.AddProfileTask profileTask = new ElasticSearchController.AddProfileTask();
        profileTask.execute(profile);
        ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
        addSessionTask.execute(session);
        solo.clickOnMenuItem("Available");
        assertTrue(solo.searchText("TESTING"));
        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");


        /**
         * Testing UseCase 04.01.01 and 04.02.01 - Searching
         * "As a borrower, I want to specify a set of keywords, and search for all things not currently
         * borrowed whose description contains all keywords." and "As a borrower, I want search results
         * to show each thing not currently borrowed with its description, owner username, and status."
         * <p/>
         * We will type things into the search bar. Then we will click search. This will then
         * bring up a new screen with the search results.
         */

        solo.typeText(0, "TESTING");
        solo.clickOnButton("Search");

        assertTrue(solo.searchText("Math", 2));
        assertTrue(solo.searchText("Tutor for linear Algebra for all university levels"));

        /**
         * Testing UseCase 01.04.01 - ViewOneSession
         * "As an owner, I want to view one of my things, its description and status."
         * <p/>
         * We will perform a click on list entry to bring us to the ViewOneSession view.
         * From here we test to see that all the buttons are present, and all the TextViews are
         * accurate
         */

        solo.assertCurrentActivity("right activity", AvailableSessionsActivity.class);
        solo.clickOnText("TESTING");
        solo.assertCurrentActivity("switched sessions", BidOnSessionActivity.class);
        solo.clickOnText("TESTING");
        assertTrue(solo.searchText("TESTING"));
        assertTrue(solo.searchText("Tutor for linear Algebra for all university levels"));
        assertTrue(solo.searchText("Test tutor"));
        assertTrue(solo.searchText("780-666-6666"));
        assertTrue(solo.searchText("test@test.test"));


        /**
         *  Testing Use Case 01.05.01 - DeleteSession
         * "As an owner, I want to delete a thing in my things."
         *
         * To test this we create a session, then change the activities to ge to the EditSession
         * view. The first test case tests if the delete occured when the user confirmed the delete.
         *
         * The second test tests the case when the user does not confirm the delete.
         */
        solo.clickOnMenuItem("Available");
        solo.assertCurrentActivity("right activity", AvailableSessionsActivity.class);

        Profile owner = MethodsController.getProfile(session.getTutorID());
        ElasticSearchController.RemoveProfileTask removeProfileTask = new ElasticSearchController.RemoveProfileTask();
        removeProfileTask.execute(owner.getProfileID());

        ElasticSearchController.RemoveSessionTask removeSessionTask = new ElasticSearchController.RemoveSessionTask();
        removeSessionTask.execute(session.getSessionID());

        solo.clickOnMenuItem("Available");

        assertFalse(solo.searchText("Math"));

    }

}