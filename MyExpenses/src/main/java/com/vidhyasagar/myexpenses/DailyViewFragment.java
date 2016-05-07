package com.vidhyasagar.myexpenses;

import android.content.Context;
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
    MyAdapter myAdapter;
    static ArrayList<ExpenseListItem> expenses;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Calendar c;
    TextView todaysDate;
    TextView dailyExpense;
    TextView weeklyLimit;
    TextView topExpense;


    class MyAdapter extends ArrayAdapter {


        public MyAdapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.layout_list_unavailable, null);
            }
            return v;
        }
    }

    public void addToDiary(View v) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.containerView,new DiaryFragment()).addToBackStack("daily").commit();
    }

    public void setUpExpensesListView(final String dateToCompare) {

        expenses.clear();
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

        ParseQuery<ParseObject> query = new ParseQuery("Expenses");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                expensesList = (ListView) getActivity().findViewById(R.id.expensesList);
                if (objects.size() != 0) {
                    for (ParseObject object : objects) {
                        String date = df.format(object.getDate("time"));
                        String currentDate = df.format(c.getTime());
                        if (date.equals(dateToCompare)) {
                            ExpenseListItem temp = new ExpenseListItem(Float.parseFloat(object.getNumber("amount").toString()),
                                    object.getString("location"), timeFormat.format(object.getDate("time")),
                                    object.getString("category"), object.getString("method"));
                            expenses.add(temp);
                        }
                    }
                    expenseListAdapter = new ExpenseListAdapter(getActivity(), R.layout.layout_list_swipe, expenses, fragmentManager);
                    expensesList.setAdapter(expenseListAdapter);
                    expenseListAdapter.notifyDataSetChanged();
                }

                if (objects.size() == 0 || expenses.size() == 0) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add("Error message");
                    myAdapter = new MyAdapter(getContext(), R.layout.layout_list_unavailable, temp);
                    expensesList.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }

                setUpStats(dateToCompare);

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

    public String findMonth (String theDate) {
        theDate = theDate.substring(3, 6);
        String toReturn = "";
        if(theDate.equals("Jan")) {
            toReturn =  "January";
        }
        else if(theDate.equals("Feb")) {
            toReturn =  "February";
        }
        else if(theDate.equals("Mar")) {
            toReturn =  "March";
        }
        else if(theDate.equals("Apr")) {
            toReturn =  "April";
        }
        else if(theDate.equals("May")) {
            toReturn =  "May";
        }
        else if(theDate.equals("Jun")) {
            toReturn =  "June";
        }
        else if(theDate.equals("Jul")) {
            toReturn =  "July";
        }
        else if(theDate.equals("Aug")) {
            toReturn =  "August";
        }
        else if(theDate.equals("Sep")) {
            toReturn =  "September";
        }
        else if(theDate.equals("Oct")) {
            toReturn =  "October";
        }
        else if(theDate.equals("Nov")) {
            toReturn =  "November";
        }
        else if(theDate.equals("Dec")) {
            toReturn =  "December";
        }
        return toReturn;
    }

    public void setUpStats(String theDate) {
//        Log.i("applog", "setting up stats again");
        topExpense = (TextView) getActivity().findViewById(R.id.topExpense);
        weeklyLimit = (TextView) getActivity().findViewById(R.id.weeklyLimit);
        dailyExpense = (TextView) getActivity().findViewById(R.id.dailyExpense);

        float topExpenseSum = 0;
        float topMostExpense = 0;


        for(ExpenseListItem item : this.expenses) {
            if(item.expenseAmount > topMostExpense) {
                topMostExpense = item.expenseAmount;
            }
            topExpenseSum = topExpenseSum + item.expenseAmount;
        }
        dailyExpense.setText("Daily Expense : $ " + String.valueOf(topExpenseSum));
        topExpense.setText("Top Expense : $ " + String.valueOf(topMostExpense));


        String theMonth = findMonth(theDate);
        Log.i("applog", "The month is " + theMonth);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Budgets");
        query.whereEqualTo("month", theMonth);
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null) {
                    e.printStackTrace();
                }
                else {
                    if(objects.size() == 0) {
                        Log.i("applog", "no reuslts found for query");
                    }
                    for(ParseObject object : objects) {
                        float temp = Float.parseFloat(object.getNumber("amount").toString());
                        temp = temp / 4;
                        weeklyLimit.setText("Weekly Limit : " +  String.valueOf(temp));
                    }
                }
            }
        });

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

        setUpExpensesListView(formattedDate);
//        setUpStats();
    }

    @Override
    public void onPause() {
        super.onPause();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        todaysDate.setText(formattedDate);
    }
}
