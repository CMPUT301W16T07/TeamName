package com.teamname.tutortrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import javax.net.ssl.SSLSessionBindingListener;

/**
 * malba @12/2/2016
 * For search use cases.
 */

public class SearchTest extends ActivityInstrumentationTestCase2 {

    /**
     *	US 04.01.01 - As a borrower, I want to specify a set of keywords, and search for all things not currently
     *                 borrowed whose description contains all keywords.
     *	Assumes that the search bar exists in the main Activity and does not have its own View.
     *	Assumes the search updates the existing Array adapter with the new search information
     *
     */
    private void createSearch(String searchText){
        assertNotNull(activity.findViewById(com.teamname.tutortrader.R.id.search));
        searchInput.setText(searchText);
        ((Button) activity.findViewById(com.teamneam.tutortrader.R.id.search)).performClick();
    }

    public void testSearchOneWord(){
        TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
        createSession("Math", "Tutor for highschool math classes.");
        createSession("Math", "Tutor for university math classes.");
        createSearch("highschool")

        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(1, arrayAdapter.getCount());
        Session session = arrayAdapter.getItem(arrayAdapter.getCount() - 1);
        assertEquals("Should be math title", session.getTitle(), "Math");
        assertEquals("Should be the description for highschool math session", session.getDescription(), "Tutor for highschool math classes.");
    }

    /*
    * should only get the Session with both 'highschool' and 'math' in the description
     */
    public void testSearchTwoWords(){
        TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
        createSession("Math", "Tutor for highschool math classes.");
        createSession("Math", "Tutor for university math classes.");
        createSession("Biology", "Tutor for biology 101, 102");

        createSearch("highschool math");

        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(1, arrayAdapter.getCount());

        Session session = arrayAdapter.getItem(arrayAdapter.getCount()-1);

        assertEquals("Should be math, highschool math", session.getTitle(), "Math");
        assertEquals("Should be math, highschool math",session.getDescription(), "Tutor for highschool math classes.");

    }

    /**
     *	check that empty list is empty.
     *
     *
     */
    public void testSearchEmpty(){
        TutorTradeAcitivity tta = (TutorTradeAcitivity)getActivity();
        createSession("Math", "Tutor for highschool math classes.");

        createSearch("nothing");

        ArrayAdapter<Session> arrayAdapter = tta.getAdapter();
        assertEquals(0, arrayAdapter.getCount());

    }
}