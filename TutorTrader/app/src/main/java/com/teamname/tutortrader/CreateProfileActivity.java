package com.teamname.tutortrader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateProfileActivity extends MethodsController {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);

        final Button saveButton = (Button) findViewById(R.id.saveButton);
        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        final EditText newUsername = (EditText) findViewById(R.id.editUsername);
        final EditText newEmail = (EditText) findViewById(R.id.editEmail);
        final EditText newPhone = (EditText) findViewById(R.id.editPhone);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid = verifyFields();
                if (valid) {
                    Profile newProfile;
                    //newProfile = new Profile("JIM username","phoneoneone phone","HELLO email");
                    //profiles.add(newProfile);

                    //saveInFile(USERFILE, profiles);
                    newProfile= new Profile(newUsername.getText().toString(),newEmail.getText().toString(),newPhone.getText().toString());

                    profiles.add(newProfile);
                    saveInFile(USERFILE, profiles);
                    currentProfile = profiles.get(0);
                    loadSessions(SESSIONSFILE);
                    setResult(RESULT_OK);
                    Intent intent = new Intent(CreateProfileActivity.this, AvailableSessionsActivity.class);
                    startActivity(intent);
                    //finish();

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // finish();

            }
        });

    }

    public boolean verifyFields () {
        //Boolean validFields = false;
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        EditText newUsername = (EditText) findViewById(R.id.editUsername);
        EditText newEmail = (EditText) findViewById(R.id.editEmail);
        EditText newPhone = (EditText) findViewById(R.id.editPhone);

        if ((!newUsername.getText().toString().equals("")) && (!newEmail.getText().toString().equals("")) && (!newPhone.getText().toString().equals(""))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}