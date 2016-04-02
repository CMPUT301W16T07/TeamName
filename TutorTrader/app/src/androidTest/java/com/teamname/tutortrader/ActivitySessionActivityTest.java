package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * This will test the addSessionActivity by adding a new activity and see that it has been
 * created
 *
 * @see AddSessionActivity for more details about what it does
 */
public class ActivitySessionActivityTest extends ActivityInstrumentationTestCase2 {


    Instrumentation instrumentation;
    Activity activity;
    EditText titleInput;
    EditText descriptionInput;
    EditText searchInput;
    Solo solo;

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
    public ActivitySessionActivityTest() {
        super(AvailableSessionsActivity.class);
    }

    /**
     * Testing "Things" Use Cases
     */


    /** USECASE 1 - AddSession
     *  createSession(title, description) fills in the input text field and
     *  clicks the 'save' button for the activity under test:
     */
    public void testViewAvailable() {
        Profile profile = new Profile("Test tutor", "test@test.test", "780-666-6666");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bm1 = Bitmap.createBitmap(1,2, conf);
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels",profile.getProfileID(),bm1 );

        ElasticSearchController.AddProfileTask profileTask = new ElasticSearchController.AddProfileTask();
        profileTask.execute(profile);
        ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
        addSessionTask.execute(session);
        solo.clickOnMenuItem("Available");
        assertTrue(solo.searchText("Math"));
        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");
    }

    /**
     * Testing UseCase 01.04.01 - ViewOneSession
     * "As an owner, I want to view one of my things, its description and status."
     * <p/>
     * We will perform a click on list entry to bring us to the ViewOneSession view.
     * From here we test to see that all the buttons are present, and all the TextViews are
     * accurate
     */
    public void testViewOneSession() {
        solo.assertCurrentActivity("right activity", AvailableSessionsActivity.class);

        solo.clickOnText("Math");

        assertTrue(solo.searchText("Math"));
        assertTrue(solo.searchText("Tutor for linear Algebra for all university levels"));
        assertTrue(solo.searchText("Test tutor"));
        assertTrue(solo.searchText("780-666-6666"));
        assertTrue(solo.searchText("test@test.test"));

    }

    /**
     * Testing UseCase 04.01.01 and 04.02.01 - Searching
     * "As a borrower, I want to specify a set of keywords, and search for all things not currently
     * borrowed whose description contains all keywords." and "As a borrower, I want search results
     * to show each thing not currently borrowed with its description, owner username, and status."
     * <p/>
     * We will type things into the search bar. Then we will click search. This will then
     * bring up a new screen with the search results.
     */
    public void testSearching(){
        solo.assertCurrentActivity("right activity", AvailableSessionsActivity.class);

        solo.typeText(0, "Math");
        solo.clickOnButton("Search");

        assertTrue(solo.searchText("Math", 2));
        assertTrue(solo.searchText("Tutor for linear Algebra for all university levels"));
    }


}