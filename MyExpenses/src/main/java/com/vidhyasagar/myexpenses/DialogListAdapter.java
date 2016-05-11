package com.vidhyasagar.myexpenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidhyasagar on 5/11/2016.
 */
public class DialogListAdapter extends ArrayAdapter {

    ArrayList<DialogListItem> items;
    int resource;

    public DialogListAdapter(Context context, int resource, ArrayList<DialogListItem> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View row = convertView;
        DialogListItem theItem = items.get(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(resource, parent, false);

        TextView expenseLocation = (TextView) row.findViewById(R.id.expenseLocation);
        TextView expenseDate = (TextView) row.findViewById(R.id.expenseDate);
        TextView expenseAmount = (TextView) row.findViewById(R.id.expenseAmount);
        TextView expenseType = (TextView) row.findViewById(R.id.expenseType);

        expenseLocation.setText(theItem.getLocation());
        expenseDate.setText(theItem.getDate());
        expenseAmount.setText(theItem.getAmount());
        expenseType.setText(theItem.getMethod());

        return row;
    }
}
