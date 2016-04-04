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
 * The activity that allows a user to edit a profile.
 */
public class EditProfileActivity extends MethodsController {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        final EditText newUsername = (EditText) findViewById(R.id.editUsername);
        final EditText newEmail = (EditText) findViewById(R.id.editEmail);
        final EditText newPhone = (EditText) findViewById(R.id.editPhone);

        newUsername.setText(currentProfile.getName());
        newEmail.setText(currentProfile.getEmail());
        newPhone.setText(currentProfile.getPhone());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = verifyFields(newUsername, newEmail, newPhone);
                if (valid) {
                    currentProfile.setName(newUsername.getText().toString());
                    currentProfile.setEmail(newEmail.getText().toString());
                    currentProfile.setPhone(newPhone.getText().toString());
                    profiles.remove(0); // remove the current profile
                    profiles.add(0, currentProfile); // replace the current profile
                    saveInFile(USERFILE, profiles); // save the new current profile
                    updateElasticSearchProfile(currentProfile); // update on Elastic Search
                    setResult(RESULT_OK);
                    Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    //finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    public boolean verifyFields (EditText newUsername, EditText newEmail, EditText newPhone) {

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
