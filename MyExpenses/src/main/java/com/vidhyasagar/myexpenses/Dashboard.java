package com.vidhyasagar.myexpenses;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gordonwong.materialsheetfab.AnimatedFab;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {




    private String[] xData = { "Food", "Movies", "Women" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setBackgroundColor(Color.TRANSPARENT);
//        chart.setDrawSliceText(true);
        chart.setUsePercentValues(true);
        chart.setHoleRadius(1.0f);
//        chart.setNoDataTextDescription("Test");
        chart.setDescription("");
//        chart.setDescriptionPosition(20, 20);
//        chart.setDescriptionTextSize(3f);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(Dashboard.this,
                        xData[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend l = chart.getLegend();
//        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
        l.setWordWrapEnabled(true);
//

// set a custom value formatter
//        xAxis.setXValueFormatter(new MyCustomFormatter());


//        XAxis leftAxis = chart.getXAxis();
//
//        LimitLine ll = new LimitLine(140f, "Critical Blood Pressure");
//        ll.setLineColor(Color.RED);
//        ll.setLineWidth(4f);
//        ll.setTextColor(Color.BLACK);
//        ll.setTextSize(12f);
//        leftAxis.addLimitLine(ll);


        ArrayList<Entry> breakdown = new ArrayList<Entry>();
//        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

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
        xVals.add("Movies"); xVals.add("Groceries"); xVals.add("Food");

        PieData data = new PieData(xVals, setBreakdown);
        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);
        chart.invalidate(); // refresh




    }


}
