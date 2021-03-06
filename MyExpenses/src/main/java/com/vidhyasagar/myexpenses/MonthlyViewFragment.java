package com.vidhyasagar.myexpenses;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vidhyasagar on 4/24/2016.
 */
public class MonthlyViewFragment extends Fragment {


    private String[] xData = { "Food", "Movies", "Women" };
    private String[] categories = {"Food", "Groceries", "Other", "Rent", "Movies", "Alcohol", "Hydro"};
    private ArrayList<String> months ;
    private HashMap<String, Integer> monthHash;
    private HashMap<String, Float> monthlySpendings;
    SharedPreferences sharedPreferences;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Calendar c;
    ArrayList<Entry> breakdown;
    HashMap<String, Float> categoryBreakdown;
    PieChart chart;

    TextView monthlyBudget;
    TextView amountRemaining;
    TextView amountSpent;

    float spent;
    float budget;

    public void initializeMonths() {
        monthlySpendings = new HashMap<>();
        months = new ArrayList<>();
        monthHash = new HashMap<>();
        months.add("January");
        monthHash.put("January", 0);
        months.add("February");
        monthHash.put("February", 1);
        months.add("March");
        monthHash.put("March", 2);
        months.add("April");
        monthHash.put("April", 3);
        months.add("May");
        monthHash.put("May", 4);
        months.add("June");
        monthHash.put("June", 5);
        months.add("July");
        monthHash.put("July", 6);
        months.add("August");
        monthHash.put("August", 7);
        months.add("September");
        monthHash.put("September", 8);
        months.add("October");
        monthHash.put("October", 9);
        months.add("November");
        monthHash.put("Novmeber", 10);
        months.add("December");
        monthHash.put("December", 11);
    }

    public void addToDiary(View v) {
        fragmentTransaction = fragmentManager.beginTransaction();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        sharedPreferences.edit().putString("theDate", df.format(c.getTime())).apply();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.containerView,new DiaryFragment()).addToBackStack("monthly").commit();
    }

    public void addToDictionary(String category, ParseObject object) {
        if (categoryBreakdown.get(category) != null) {
            categoryBreakdown.put(category, categoryBreakdown.get(category) + Float.parseFloat(object.getNumber("amount").toString()));
        } else {
            categoryBreakdown.put(category, Float.parseFloat(object.getNumber("amount").toString()));
        }
    }

    public void cleanUpCategories() {
        for(String category : categories) {
            if(categoryBreakdown.get(category) == null) {
                categoryBreakdown.put(category, 0f);
            }
        }
    }

    public void showPopUpBreakDownDialog(final Entry e) {
        initializeMonths();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View breakdownView = inflater.inflate(R.layout.layout_breakdown_dialog, null);
        //Show a dialog box here which gives you a breakdown of all spending
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle("Breakdown of spending for " + categories[e.getXIndex()])
                .setIcon(R.drawable.ic_drawer_to_arrow)
                .setPositiveButton("Close", null)
                .setView(breakdownView);



        final ListView breakdownListView = (ListView) breakdownView.findViewById(R.id.breakdownList);
        if(breakdownListView == null) {
            Log.i("applog", "listview is null");
        }
        //Expans on this
        final ArrayList<DialogListItem> individualSpendings = new ArrayList<>();
        final SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        final String theMonth = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
        String tempMonth = null;
        Log.i("applog", "Dialog month is : " + theMonth);
        ParseQuery<ParseObject> query = new ParseQuery<>("Expenses");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        if((monthHash.get(theMonth) - 6) < 0) {
            query.whereContainedIn("month", months.subList(0, monthHash.get(theMonth) + 1));
            Log.i("applog", months.subList(0, monthHash.get(theMonth) + 1).toString());
            tempMonth = months.get(0);
        }
        else {
            query.whereContainedIn("month", months.subList(monthHash.get(theMonth) - 6, monthHash.get(theMonth)));
            Log.i("applog", months.subList(monthHash.get(theMonth) - 5, monthHash.get(theMonth) + 1).toString());
            tempMonth = months.get(monthHash.get(theMonth) - 5);
        }
        query.orderByAscending("month");
        query.whereEqualTo("category", categories[e.getXIndex()]);
        final String finalTempMonth = tempMonth;
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() == 0) {
                        Log.i("applog", "No such query found for the dialog");
                    }
                    else {
                        String newMonth = finalTempMonth;
                        float monthAmount = 0;
                        for(int i = 0; i < objects.size(); i++) {
                            if(newMonth.equals(objects.get(i).getString("month").toString())) {
                                monthAmount = monthAmount + Float.parseFloat(objects.get(i).getNumber("amount").toString());
                            }
                            else { //Change month, save and reset month amount
                                monthlySpendings.put(newMonth, monthAmount);
                                newMonth = objects.get(i).getString("month").toString();
                                monthAmount = 0;
                                monthAmount = monthAmount + Float.parseFloat(objects.get(i).getNumber("amount").toString());
                            }

                            if(theMonth.equals(objects.get(i).getString("month").toString())) {
                                individualSpendings.add(new DialogListItem(
                                   objects.get(i).getString("location"),
                                        objects.get(i).getNumber("amount").toString(),
                                        df.format(objects.get(i).getDate("time")),
                                        objects.get(i).getString("method").toString()
                                ));
                            }

                        }
                        monthlySpendings.put(newMonth, monthAmount);
                        Log.i("applog", "Hashmap is " + monthlySpendings.toString());
                        Log.i("applog", "Arraylist is  " + individualSpendings.toString());

                        //Values now ready for Line Graph
                        //ArrayList of DialogListItems now ready for being put into the main list
                        DialogListAdapter breakdownAdapter = new DialogListAdapter(getContext(), R.layout.layout_list_dialog, individualSpendings);
                        breakdownListView.setAdapter(breakdownAdapter);
                        //CURRENT LOCATION
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });


        //FINALLY SHOW THE DIALOG
        dialog.show();
    }

    public void setUpChartOverview() {
        chart = (PieChart) getView().findViewById(R.id.chart);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(1f);
        chart.setDrawSliceText(false);
        chart.setCenterText("Monthly Breakdown");
        chart.setCenterTextSize(13f);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(getActivity(),
                        categories[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();

                showPopUpBreakDownDialog(e);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend l = chart.getLegend();
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setEnabled(true);
        l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
        l.setWordWrapEnabled(true);

        final int[] colors = {R.color.red, R.color.green, R.color.yellow, R.color.aqua, R.color.fuchsia, R.color.maroon, R.color.lime};
//        String[] tempList = {};
//        l.setCustom(colors, categories);


        breakdown = new ArrayList<Entry>();
        categoryBreakdown = new HashMap<>();


        //Must populuate the breakdown arraylist with query results
        final SimpleDateFormat format = new SimpleDateFormat("MMM-yyyy");
        final String currentMonth = format.format(c.getTime());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Expenses");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    float totalExpenses = 0;
                    for (ParseObject object : objects) {
                        if (currentMonth.equals(format.format(object.getDate("time")))) {
                            //these objects are this months expenses now
                            totalExpenses = totalExpenses + Float.parseFloat(object.getNumber("amount").toString());
                            String objCategory = object.getString("category");
                            if (objCategory.equals("Food")) {
                                addToDictionary("Food", object);
                            }
                            else if (objCategory.equals("Groceries")) {
                               addToDictionary("Groceries", object);
                            }
                            else if(objCategory.equals("Other")) {
                                addToDictionary("Other", object);
                            }
                            else if(objCategory.equals("Rent")) {
                                addToDictionary("Rent", object);
                            }
                            else if(objCategory.equals("Alcohol")) {
                                addToDictionary("Alcohol", object);
                            }
                            else if(objCategory.equals("Movies")) {
                                addToDictionary("Movies", object);
                            }
                            else if(objCategory.equals("Hydro")) {
                                addToDictionary("Hydro", object);
                            }
                        }
                    }

                    cleanUpCategories();

                    Entry food = new Entry(((categoryBreakdown.get("Food")/totalExpenses) * 100), 0); // 0 == quarter 1
                    breakdown.add(food);
                    Entry groceries = new Entry(((categoryBreakdown.get("Groceries")/totalExpenses) * 100), 1); // 0 == quarter 1
                    breakdown.add(groceries);
                    Entry other = new Entry(((categoryBreakdown.get("Other")/totalExpenses) * 100), 2); // 0 == quarter 1
                    breakdown.add(other);
                    Entry rent = new Entry(((categoryBreakdown.get("Rent")/totalExpenses) * 100), 3); // 0 == quarter 1
                    breakdown.add(rent);
                    Entry movies = new Entry(((categoryBreakdown.get("Movies")/totalExpenses) * 100), 4); // 0 == quarter 1
                    breakdown.add(movies);
                    Entry alcohol = new Entry(((categoryBreakdown.get("Alcohol")/totalExpenses) * 100), 5); // 0 == quarter 1
                    breakdown.add(alcohol);
                    Entry hydro = new Entry(((categoryBreakdown.get("Hydro")/totalExpenses) * 100), 6); // 0 == quarter 1
                    breakdown.add(hydro);


                    PieDataSet setBreakdown = new PieDataSet(breakdown, "");
                    setBreakdown.setAxisDependency(YAxis.AxisDependency.LEFT);
                    setBreakdown.setSliceSpace(5f);
                    setBreakdown.setColors(colors, getContext());

                    ArrayList<String> xVals = new ArrayList<String>();
                    xVals.add("Food");
                    xVals.add("Groceries");
                    xVals.add("Other");
                    xVals.add("Rent");
                    xVals.add("Movies");
                    xVals.add("Alcohol");
                    xVals.add("Hydro");
//
                    PieData data = new PieData(xVals, setBreakdown);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextSize(10f);
                    chart.setData(data);
                    chart.invalidate(); // refresh

                }
            }
        });


    }

    public String formattedMonth(String month) {
        String toReturn = "";
        if(month.equals("Jan")) {
            toReturn = "January";
        }
        else if(month.equals("Feb")) {
            toReturn = "February";
        }
        else if(month.equals("Mar")) {
            toReturn = "March";
        }
        else if(month.equals("Apr")) {
            toReturn = "April";
        }
        else if(month.equals("May")) {
            toReturn = "May";
        }
        else if(month.equals("Jun")) {
            toReturn = "June";
        }
        else if(month.equals("Jul")) {
            toReturn = "July";
        }
        else if(month.equals("Aug")) {
            toReturn = "August";
        }
        else if(month.equals("Sep")) {
            toReturn = "September";
        }
        else if(month.equals("Oct")) {
            toReturn = "October";
        }
        else if(month.equals("Nov")) {
            toReturn = "November";
        }
        else if(month.equals("Dec")) {
            toReturn = "December";
        }

        return toReturn;
    }


    public void setUpStats() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        String month = monthFormat.format(c.getTime());

        monthlyBudget = (TextView) getActivity().findViewById(R.id.monthlyBudget);
        amountRemaining = (TextView) getActivity().findViewById(R.id.amountRemaining);
        amountSpent = (TextView) getActivity().findViewById(R.id.amountSpent);

//        month = formattedMonth(month);
        final ParseQuery<ParseObject> newQuery = new ParseQuery<ParseObject>("Budgets");
        newQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        newQuery.whereEqualTo("month", formattedMonth(month));

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Expenses");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("month", month);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    spent = 0;
                    if(objects.size() == 0) {
                        Log.i("applog", "expenses come to 0");
                    }
                    for (ParseObject object : objects) {
                        spent = spent + Float.parseFloat(object.getNumber("amount").toString());
                    }
                }
                newQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e != null) {
                            e.printStackTrace();
                        }
                        else {
                            for(ParseObject object : objects) {
                                budget = Float.parseFloat(object.getNumber("amount").toString());
                            }
                        }

                        monthlyBudget.setText("Monthly Budget : $ " + String.valueOf(Math.round(budget * 100.00)/100.00));
                        amountRemaining.setText("Amount Remaining : $ " + String.valueOf(Math.round((budget - spent)* 100.00)/100.00));
                        amountSpent.setText("Amount Spent : $ " + String.valueOf(Math.round(spent * 100.00)/100.00));
                    }
                });
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        c = Calendar.getInstance();
        View x = inflater.inflate(R.layout.fragment_monthly_view,null);
        FloatingActionButton fab = (FloatingActionButton) x.findViewById(R.id.addDiaryButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDiary(v);
            }
        });
        sharedPreferences = getActivity().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpChartOverview();
        setUpStats();
    }
}
