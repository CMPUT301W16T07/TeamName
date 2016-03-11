package com.teamname.tutortrader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MySessionsActivity extends MethodsController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sessions);

        //Adding a new session
        Button addNewSession = (Button) findViewById(R.id.addNewSession);
        addNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySessionsActivity.this, AddSessionActivity.class);
                startActivity(intent);
            }
        });

        //Selecting a session from list
        // TODO: pass the index of the list item to fill fields in the ViewOneSession activity
        Intent intent = new Intent(MySessionsActivity.this, ViewOneSessionActivity.class);


    }
}
