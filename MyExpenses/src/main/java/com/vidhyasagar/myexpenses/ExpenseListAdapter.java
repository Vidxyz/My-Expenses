package com.vidhyasagar.myexpenses;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vidhyasagar on 4/27/2016.
 */
public class ExpenseListAdapter extends ArraySwipeAdapter {

    Context context;
    int resource;
    ArrayList<ExpenseListItem> items = null;

    public void setupEditEntry(View view, int position) {

        ImageView editButton = (ImageView) view.findViewById(R.id.listEditButton);
        editButton.setClickable(true);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("applog", "edit pressed");
            }
        });
    }



    public void setupDeleteEntry(View view, final int position) {
        ImageView deleteButton = (ImageView) view.findViewById(R.id.listDeleteButton);
        deleteButton.setClickable(true);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("applog", "delete pressed");
                DailyViewFragment.expenses.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public ExpenseListAdapter(Context context, int resource, ArrayList<ExpenseListItem> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // Get the data item for this position
        ExpenseListItem item = items.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_list_swipe, parent, false);
        }

        // Lookup view for data population
        TextView expenseType = (TextView) row.findViewById(R.id.expenseType);
        TextView expenseAmount = (TextView) row.findViewById(R.id.expenseAmount);
        TextView expenseLocation = (TextView) row.findViewById(R.id.expenseLocation);
        TextView expenseTime = (TextView) row.findViewById(R.id.expenseTime);
        ImageView expenseIcon = (ImageView) row.findViewById(R.id.expenseIcon);

        // Populate the data into the template view using the data object
        expenseType.setText(item.expenseType);
        expenseAmount.setText(Float.toString(item.expenseAmount));
        expenseLocation.setText(item.expenseLocation);
        expenseTime.setText(item.expenseTime);

        expenseIcon.setImageResource(R.drawable.ic_dashboard);

        setupEditEntry(row, position);
        setupDeleteEntry(row, position);

        // Return the completed view to render on screen
        return row;

    }

}
