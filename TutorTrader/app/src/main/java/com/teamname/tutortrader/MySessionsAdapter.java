package com.teamname.tutortrader;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MJ Alba on 2016-03-08.
 *
 * This class represents a Current Bid as a list item. Its purpose is
 * to display Current Bids as list items according to the markup defined by
 * current_bids_list_item.xml.
 */
public class MySessionsAdapter extends ArrayAdapter<Session> {

    private Context context;
    private ArrayList<Session> arrayList;

    public MySessionsAdapter(Context context, ArrayList<Session> arrayList) {
        super(context, R.layout.my_sessions_list, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    // define how to get a view for a ListView
    // based on code by hmkcode found on http://hmkcode.com/android-custom-listview-items-row/
    @Override
    public View getView(int index, View convert, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySessionsView = inflater.inflate(R.layout.my_sessions_list, parent, false);

        TextView sessionView = (TextView) mySessionsView.findViewById(R.id.sessionTitle);
        TextView descriptionView = (TextView) mySessionsView.findViewById(R.id.description);
        //TextView statusView = (TextView) availableSessionsView.findViewById(R.id.status);

        String sessionString = "<b>" + arrayList.get(index).getTitle() + "</b> <i>by "  + arrayList.get(index).tutor.getName() + "</i>";
        String descriptionString = arrayList.get(index).getDescription();
        //String statusString = "Bid Status: <b>" + arrayList.get(index).getStatus() + "</b>.";

        sessionView.setText(Html.fromHtml(sessionString));
        descriptionView.setText(Html.fromHtml(descriptionString));
        // statusView.setText(Html.fromHtml(statusString));

        return mySessionsView;
    }
}
