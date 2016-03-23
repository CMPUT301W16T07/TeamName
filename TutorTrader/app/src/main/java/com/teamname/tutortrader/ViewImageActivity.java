package com.teamname.tutortrader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewImageActivity extends MethodsController {
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        Intent intent = getIntent();
        final String index_receive = intent.getStringExtra("index");
        index = Integer.parseInt(index_receive);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewImageActivity.this, BidOnSessionActivity.class);
                int properIndex = sessionsOfInterest.indexOf(sessions.get(index));
                intent.putExtra("index", String.valueOf(properIndex));
                startActivity(intent);
            }
        });
        TextView sessionTitle = (TextView) findViewById(R.id.sessionTitle);
        sessionTitle.setText(sessions.get(index).getTitle().toString());
        ImageView viewImage = (ImageView) findViewById(R.id.viewImage);

        viewImage.setImageBitmap(sessions.get(index).getThumbnail());
    }
}
