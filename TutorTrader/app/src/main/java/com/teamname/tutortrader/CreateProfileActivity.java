package com.teamname.tutortrader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The activity that allows a user to create a profile.
 */
public class CreateProfileActivity extends MethodsController{


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

                    newProfile= new Profile(newUsername.getText().toString(),newEmail.getText().toString(),newPhone.getText().toString());
                    newProfile.setDefaultUser(Boolean.FALSE);

                    profiles.remove(0);
                    profiles.add(newProfile);
                    saveInFile(USERFILE, profiles);
                    ElasticSearchController.AddProfileTask addProfileTask = new ElasticSearchController.AddProfileTask();
                    addProfileTask.execute(newProfile);
                    loadElasticSearch(); // load the newest addition
                    currentProfile = profiles.get(0);

                    setResult(RESULT_OK);
                    Intent intent = new Intent(CreateProfileActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfileActivity.this, AvailableSessionsActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean verifyFields () {

        EditText newUsername = (EditText) findViewById(R.id.editUsername);
        EditText newEmail = (EditText) findViewById(R.id.editEmail);
        EditText newPhone = (EditText) findViewById(R.id.editPhone);

        Boolean valid = true; // assume fields are valid
        Context context = getApplicationContext();
        CharSequence text = "Sorry! Something went wrong."; // warning text if invalid
        int duration = Toast.LENGTH_SHORT; // warning length

        // validate every field
        if (newUsername.getText().toString().equals("")) {
            text = "Invalid Username!";
            valid = false;
        } else if (MethodsController.profileExists(newUsername.getText().toString(), currentProfile.getName())) {
            text = "Username already exists!";
            valid = false;
        } else if (newEmail.getText().toString().equals("")) {
            text = "Invalid Email!";
            valid = false;
        } else if (newPhone.getText().toString().equals("")) {
            text = "Invalid Phone Number!";
            valid = false;
        }

        // show toast if necessary
        if (!valid) {
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        // return true if valid, false if not
        return valid;
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
