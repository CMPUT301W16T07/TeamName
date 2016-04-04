package com.teamname.tutortrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This activity is launched from BidOnSessionActivity and allows
 * a user to view the image associated with a given session.
 * @see BidOnSessionActivity
 */
public class ViewImageActivity extends MethodsController {
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        Intent intent = getIntent();
        String index_receive = intent.getStringExtra("index");
        index = Integer.parseInt(index_receive);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView sessionTitle = (TextView) findViewById(R.id.sessionTitle);
        sessionTitle.setText(sessions.get(index).getTitle());
        ImageView viewImage = (ImageView) findViewById(R.id.viewImage);
        viewImage.setImageBitmap(sessions.get(index).getThumbnail());
    }
}
