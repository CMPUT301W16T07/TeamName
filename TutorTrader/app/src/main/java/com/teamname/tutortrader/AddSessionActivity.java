package com.teamname.tutortrader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddSessionActivity extends MethodsController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_session);
        Button cancelAddSession = (Button) findViewById(R.id.cancelAddSession);
        cancelAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSessionActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid;
                valid = verifyFields();
                if (valid) {
                    EditText subjectEdit = (EditText) findViewById(R.id.subjectEdit);
                    EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
                    // TODO: implement Tutor
                    Session newSession = new Session(subjectEdit.getText().toString(),descriptionEdit.getText().toString(),currentProfile);
                    sessions.add(newSession);
                    saveInFile(SESSIONSFILE, sessions);
                    Intent intent = new Intent(AddSessionActivity.this, MySessionsActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * checks to see if the input fields are valid
     * @return boolean variable true if valid, false if invalid
     */
    public boolean verifyFields () {
        //Boolean validFields = false;
        EditText subjectEdit = (EditText) findViewById(R.id.subjectEdit);
        EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);

        if ((!subjectEdit.getText().toString().equals("")) && (!descriptionEdit.getText().toString().equals(""))) {
            return true;
        }
        return false;
    }
}
