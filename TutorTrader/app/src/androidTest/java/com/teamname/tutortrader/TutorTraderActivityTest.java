package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by taylorarnett on 2016-02-10.
 */
public class TutorTraderActivityTest extends ActivityInstrumentationTestCase2 {

    Instrumentation instrumentation;
    Activity activity;
    EditText titleInput;
    EditText descriptionInput;

    public TutorTraderActivityTest(Class activityClass) {
        super(activityClass);
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
     * @param title a string input of the subject of the tutoring session
     * @param description a string input of the description of the tutoring session.
     */
    private void createSession(String title, String description) {
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.save));
        titleInput.setText(title);
        descriptionInput.setText(description);
        ((Button) activity.findViewById(com.teamname.tutortrader.R.id.save)).performClick();
    }

    /** USECASE 1 = AddSession
     * Testing if a session is added after all text fields are inputted correctly.
     * This Tests the UI
     */
    public void testAddSessionValid() {
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        int oldLength = tta.getAdapter().getCount();
        createSession("Math", "Tutor for linear Algebra for all university levels");
        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(oldLength +1, arrayAdapter.getCount());

        assertTrue("Did you add a Session object?",
                arrayAdapter.getItem(arrayAdapter.getCount() - 1) instanceof Session);

        Session session = arrayAdapter.getItem(arrayAdapter.getCount()-1);
        assertEquals("this is the title we expected", session.getTitle(), "Math");
        assertEquals("this is the description we expected", session.getDescription(),
                "Tutor for linear Algebra for all university levels");
    }

    /** USECASE 1 = AddSession
     * This tests use case 1 but in the case where the input fields are incomplete
     * This tests the UI
     */
    public void testAddSessionIncomplete() {
        TutorTradeActivity tta = (TutorTradeActivity)getActivity();
        int oldLength = tta.getAdapter().getCount();
        createSession("Math", null);
        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(oldLength, arrayAdapter.getCount());

        assertFalse("Did you add a Session object?",
                arrayAdapter.getItem(arrayAdapter.getCount() - 1) instanceof Session);

    }

    /**
     * Testing UseCase 01.02.01 - ViewSessions
     * "As an owner, I want to view a list of all my sessions, and their descriptions and statuses."
     *
     */
}
