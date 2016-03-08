package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ALI on 2016-03-07.
 */
public class EditSessionActivity extends AppCompatActivity {

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_session);
    Button cancelEditSession = (Button) findViewById(R.id.cancelEditSession);
    cancelEditSession.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditSessionActivity.this, MySessionsActivity.class);
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
                EditText subjectEdit = ((EditText)findViewById(R.id.subjectEdit));
                //TODO: implement setText
                subjectEdit.setText("TODO: Enter what they had already");
                EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
                //TODO: implement setText
                descriptionEdit.setText("TODO: Enter what they had already");
                // TODO: implement Tutor
                //Session newSession = new Session(subjectEdit.getText().toString(),descriptionEdit.getText().toString(),tutor);

                Intent intent = new Intent(EditSessionActivity.this, MySessionsActivity.class);
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