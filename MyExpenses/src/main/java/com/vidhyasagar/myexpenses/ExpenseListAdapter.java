package com.vidhyasagar.myexpenses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
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
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public void setupEditEntry(View view, final int position) {
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        ImageView editButton = (ImageView) view.findViewById(R.id.listEditButton);
        editButton.setClickable(true);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("location", items.get(position).expenseLocation).apply();
                sharedPreferences.edit().putString("category", items.get(position).expenseIcon).apply();
                sharedPreferences.edit().putString("method", items.get(position).expenseType).apply();
                sharedPreferences.edit().putFloat("amount", items.get(position).expenseAmount).apply();
                sharedPreferences.edit().putString("theDate", df.format(items.get(position).expenseDate)).apply();

                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                DiaryFragment dFragment = new DiaryFragment();
                fragmentTransaction.replace(R.id.containerView, dFragment).addToBackStack("daily").commit();
            }
        });
    }



    public void setupDeleteEntry(View view, final int position) {
        ImageView deleteButton = (ImageView) view.findViewById(R.id.listDeleteButton);
        deleteButton.setClickable(true);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete query from the database and call notify data set changed
                ExpenseListItem item = items.get(position);
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Expenses");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo("location", item.expenseLocation);
                query.whereEqualTo("method", item.expenseType);
                query.whereEqualTo("category", item.expenseIcon);
                query.whereEqualTo("amount", item.expenseAmount);

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null) {
                            if(objects.size() == 0) {
                                Log.i("applog", "delete stack is empty");
                            }

                            for(ParseObject object : objects) {
                                object.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e != null) {
                                            e.printStackTrace();
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Deleted expense!", Toast.LENGTH_LONG).show();
                                            DashboardFragment.refresh();
                                        }
                                    }
                                });
                            }

                        }else {
                            e.printStackTrace();
                        }
                    }
                });

                DailyViewFragment.expenses.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public ExpenseListAdapter(Context context, int resource, ArrayList<ExpenseListItem> items, FragmentManager fragmentManager) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
        this.fragmentManager = fragmentManager;
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
