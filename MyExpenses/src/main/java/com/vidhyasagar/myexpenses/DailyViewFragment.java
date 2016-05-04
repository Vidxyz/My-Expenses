package com.vidhyasagar.myexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Vidhyasagar on 4/24/2016.
 */
public class DailyViewFragment extends Fragment {

    ListView expensesList;
    ExpenseListAdapter expenseListAdapter;
    static ArrayList<ExpenseListItem> expenses;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Calendar c;

    TextView todaysDate;

    public void addToDiary(View v) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.containerView,new DiaryFragment()).addToBackStack("daily").commit();
    }

    public void setUpExpensesListView(final String dateToCompare) {

        expenses.clear();
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        ParseQuery<ParseObject> query = new ParseQuery("Expenses");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    String date = df.format(object.getDate("time"));
                    String currentDate = df.format(c.getTime());
                    if (date.equals(dateToCompare)) {
                        ExpenseListItem temp = new ExpenseListItem(Float.parseFloat(object.getNumber("amount").toString()),
                                object.getString("location"), object.getString("time"), object.getString("category"), object.getString("method"));
                        expenses.add(temp);
                    }
                }
                expenseListAdapter.notifyDataSetChanged();
            }
        });


    }

    public void previousDate(View view) {
        c.setTime(c.getTime());
        c.add(Calendar.DATE, -1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        todaysDate.setText(formattedDate);
        setUpExpensesListView(formattedDate);

    }

    public void nextDate(View view) {
        c.setTime(c.getTime());
        c.add(Calendar.DATE, 1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        todaysDate.setText(formattedDate);
        setUpExpensesListView(formattedDate);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        View x =  inflater.inflate(R.layout.fragment_daily_view,null);
        FloatingActionButton fab = (FloatingActionButton) x.findViewById(R.id.addDiaryButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDiary(v);
            }
        });
        todaysDate = (TextView) x.findViewById(R.id.todaysDate);
        c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        todaysDate.setText(formattedDate);

        ImageView previousButton = (ImageView) x.findViewById(R.id.previousButton);
        ImageView nextButton = (ImageView) x.findViewById(R.id.nextButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousDate(v);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDate(v);
            }
        });

        //Adding to listview
        expenses = new ArrayList<>();
        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        expensesList = (ListView) getActivity().findViewById(R.id.expensesList);
        expenseListAdapter = new ExpenseListAdapter(getActivity(), R.layout.layout_list_swipe, expenses, fragmentManager);
        expensesList.setAdapter(expenseListAdapter);
        setUpExpensesListView(formattedDate);
    }

    @Override
    public void onPause() {
        super.onPause();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        todaysDate.setText(formattedDate);
    }
}
