package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * This will test the addSessionActivity by adding a new activity and see that it has been
 * created
 *
 * @see AddSessionActivity for more details about what it does
 */
public class AddSessionActivityTest extends ActivityInstrumentationTestCase2 {


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
    public AddSessionActivityTest() {
        super(AvailableSessionsActivity.class);
    }

    /**
     * Testing "Things" Use Cases
     */

    /**
     * Testing UseCase 01.01.01 - AddSession
     * "As an owner, I want to add a thing in my things, each denoted with a clear,
     *  suitable description."
     */

    /** USECASE 1 - AddSession
     *  createSession(title, description) fills in the input text field and
     *  clicks the 'save' button for the activity under test:
     */
    public void testAddSessionValid() {
        AvailableSessionsActivity msa = (AvailableSessionsActivity)getActivity();
        Profile profile = new Profile("Name", "Phone", "Email");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bm1 = Bitmap.createBitmap(1,2, conf);
        Session session = new Session("Math", "Tutor for linear Algebra for all university levels",profile.getProfileID(),bm1 );

        ElasticSearchController.AddSessionTask addSessionTask = new ElasticSearchController.AddSessionTask();
        addSessionTask.execute(session);
        solo.clickOnMenuItem("Available");
        assertTrue(solo.searchText("Math"));
        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");
    }



}