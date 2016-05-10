package com.vidhyasagar.myexpenses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vidhyasagar on 5/9/2016.
 */
public class CalendarTabFragment extends Fragment{

    CalendarView calendarView;
    Button jumpButton;
    String dateSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return  inflater.inflate(R.layout.fragment_view_calendar,null);
    }

    @Override
    public void onStart() {
        super.onStart();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateSelected = null;

        jumpButton = (Button) getActivity().findViewById(R.id.jumpButton);

        calendarView = (CalendarView) getActivity().findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(2);
        long date = calendarView.getDate();
        dateSelected = dateFormat.format(new Date(date));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                Log.i("applog", dateString);
                dateSelected = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                Log.i("applog - date selected", dateSelected);
            }
        });

        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("applog", dateSelected);
                Log.i("applog", "Going to this date's list view");
            }
        });
    }
}
