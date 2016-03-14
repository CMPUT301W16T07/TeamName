package com.teamname.tutortrader;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateProfileActivity extends MethodsController {

    Button saveButton = (Button) findViewById(R.id.saveButton);
    Button cancelButton = (Button) findViewById(R.id.cancelButton);
    final EditText newUsername = (EditText) findViewById(R.id.editUsername);
    final EditText newEmail = (EditText) findViewById(R.id.editEmail);
    final EditText newPhone = (EditText) findViewById(R.id.editPhone);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid = verifyFields();
                if (valid) {
                    currentProfile.setName(newUsername.getText().toString());
                    currentProfile.setEmail(newEmail.getText().toString());
                    currentProfile.setPhone(newPhone.getText().toString());
                    saveProfile(currentProfile);
                    setResult(RESULT_OK);
                    finish();
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

    public boolean verifyFields () {
        //Boolean validFields = false;

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
