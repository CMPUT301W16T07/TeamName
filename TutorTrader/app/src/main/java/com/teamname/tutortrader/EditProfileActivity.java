package com.teamname.tutortrader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by taylorarnett on 2016-03-01.
 *
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

        //final MethodsController instance = MethodsController.getInstance();
        //final Profile currentProfile = instance.getCurrentProfile();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = verifyFields(newUsername, newEmail, newPhone);
                if (valid) {
                    currentProfile.setName(newUsername.getText().toString());
                    currentProfile.setEmail(newEmail.getText().toString());
                    currentProfile.setPhone(newPhone.getText().toString());
                    int index = profiles.indexOf(currentProfile);
                    profiles.remove(index);
                    profiles.add(index, currentProfile);
                    saveInFile(USERFILE, profiles);
                    setResult(RESULT_OK);
                    Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                    startActivity(intent);
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
        //Boolean validFields = false;

        if ((!newUsername.getText().toString().equals("")) && (!newEmail.getText().toString().equals("")) && (!newPhone.getText().toString().equals(""))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
