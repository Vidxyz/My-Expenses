package com.vidhyasagar.myexpenses;


import android.content.Intent;
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

import java.util.ArrayList;

/**
 * Created by Vidhyasagar on 4/24/2016.
 */
public class MonthlyViewFragment extends Fragment {

    private String[] xData = { "Food", "Movies", "Women" };
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public void addToDiary(View v) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.containerView,new DiaryFragment()).addToBackStack("monthly").commit();
    }

    public void setUpChartOverview() {
        PieChart chart = (PieChart) getView().findViewById(R.id.chart);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setHoleRadius(2.0f);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(getActivity(),
                        xData[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend l = chart.getLegend();
//        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
        l.setWordWrapEnabled(true);

        ArrayList<Entry> breakdown = new ArrayList<Entry>();

        Entry c1e1 = new Entry(23.000f, 0); // 0 == quarter 1
        breakdown.add(c1e1);
        Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
        breakdown.add(c1e2);
        Entry c1e3 = new Entry(27.0f, 2);
        breakdown.add(c1e3);


        PieDataSet setBreakdown = new PieDataSet(breakdown, "Breakdown");
        setBreakdown.setAxisDependency(YAxis.AxisDependency.LEFT);
        setBreakdown.setSliceSpace(5f);
        setBreakdown.setColors(ColorTemplate.VORDIPLOM_COLORS);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Movies");
        xVals.add("Groceries");
        xVals.add("Food");

        PieData data = new PieData(xVals, setBreakdown);
        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);
        chart.invalidate(); // refresh
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        View x = inflater.inflate(R.layout.fragment_monthly_view,null);
        FloatingActionButton fab = (FloatingActionButton) x.findViewById(R.id.addDiaryButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDiary(v);
                Log.i("applog", "This button pressed");
            }
        });
        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("applog", "This previous button pressed");
        setUpChartOverview();


    }
}
