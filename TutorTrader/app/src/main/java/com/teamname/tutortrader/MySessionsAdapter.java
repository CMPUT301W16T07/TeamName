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
 *
 * This class represents a User's Session as a list item. Its purpose is
 * to display MySessions as list items according to the markup defined by
 * my_sessions_list.xml.
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
        TextView statusView = (TextView) mySessionsView.findViewById(R.id.status);
        TextView bidView = (TextView) mySessionsView.findViewById(R.id.bids);
        Integer pendingBidsCount;

        // get current number of pending bids (bidsCount or bidsCount - 1 if session is booked)
        if (arrayList.get(index).getStatus().equals("booked")) {
            pendingBidsCount = arrayList.get(index).getBidsCount() - 1;
        } else {
            pendingBidsCount = arrayList.get(index).getBidsCount();
        }
        String sessionString;
        if (MethodsController.Connectivity) {
            // need a separate condition checker becuse getProfile uses elastic search
            Profile tutor = MethodsController.getProfile(arrayList.get(index).getTutorID());
            sessionString = "<b>" + arrayList.get(index).getTitle() + "</b> <i>by " + tutor.getName() + "</i>";
        } else {
            sessionString = "<b>" + arrayList.get(index).getTitle() + "</b> <i> [[OFFLINE]] </i>";
        }
        String descriptionString = arrayList.get(index).getDescription();
        String statusString = "Session Status: <b>" + arrayList.get(index).getStatus() + "</b>";
        String bidString = "Pending Bids: <b>" + pendingBidsCount + "</b>";

        sessionView.setText(Html.fromHtml(sessionString));
        descriptionView.setText(Html.fromHtml(descriptionString));
        statusView.setText(Html.fromHtml(statusString));
        bidView.setText(Html.fromHtml(bidString));

        return mySessionsView;
    }
}
