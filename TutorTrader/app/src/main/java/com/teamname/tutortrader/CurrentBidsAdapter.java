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
 * This class represents a Current Bid as a list item. Its purpose is
 * to display Current Bids as list items according to the markup defined by
 * current_bids_list_item.xml.
 */
public class CurrentBidsAdapter extends ArrayAdapter<Bid> {

    private Context context;
    private ArrayList<Bid> arrayList;

    public CurrentBidsAdapter(Context context, ArrayList<Bid> arrayList) {
        super(context, R.layout.current_bids_list_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    // define how to get a view for a ListView
    // based on code by hmkcode found on http://hmkcode.com/android-custom-listview-items-row/
    @Override
    public View getView(int index, View convert, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View currentBidView = inflater.inflate(R.layout.current_bids_list_item, parent, false);

        TextView sessionView = (TextView) currentBidView.findViewById(R.id.sessionTitle);
        TextView amountView = (TextView) currentBidView.findViewById(R.id.amount);
        TextView statusView = (TextView) currentBidView.findViewById(R.id.status);

        String sessionString = "TEST SESSION <i>by TEST TUTOR</i>";
        String amountString = "Bid: <b>$" + arrayList.get(index).getAmount() + "</b> per hour.";
        String statusString = "Bid Status: <b>" + arrayList.get(index).getStatus() + "</b>.";

        sessionView.setText(Html.fromHtml(sessionString));
        amountView.setText(Html.fromHtml(amountString));
        statusView.setText(Html.fromHtml(statusString));

        return currentBidView;
    }
}
