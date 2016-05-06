package com.vidhyasagar.myexpenses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidhyasagar on 5/5/2016.
 */
public class SetBudgetFragment extends Fragment {

    SeekBar amountSeekBar;
    EditText budgetAmount;
    TextView perYear, perWeek;
    ArrayList<String> listItems;
    ArrayList<String> months;
    ArrayAdapter arrayAdapter;
    ArrayAdapter monthAdapter;
    ListView listView;
    RelativeLayout relativeLayout;
    int amount;

    public void addMonths() {
        months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
    }

    public void disableKeyboard(View view) {
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0); //0 is for no custom flags
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_budget_budget,null);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        perWeek = (TextView) getActivity().findViewById(R.id.perWeek);
        perYear = (TextView) getActivity().findViewById(R.id.perYear);
        budgetAmount = (EditText) getActivity().findViewById(R.id.budgetAmount);
        amountSeekBar = (SeekBar) getActivity().findViewById(R.id.amountSeekBar);

        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableKeyboard(v);
            }
        });

        addMonths();


        listView = (ListView) getActivity().findViewById(R.id.listView);
        listItems = new ArrayList<>();
        listItems.add("Add a month...");
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(arrayAdapter);

        budgetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    Log.i("applog", s.toString());
                    perWeek.setText(String.valueOf(Float.parseFloat(s.toString()) / 4) + " / Week");
                    perYear.setText(String.valueOf(Float.parseFloat(s.toString()) * 12) + " / Year");
                }
                else  {
                    Log.i("applog", s.toString());
                    perWeek.setText("0 / Week");
                    perYear.setText("0 / Year");
                }
//                amountSeekBar.setProgress((int) Float.parseFloat(s.toString()));
            }
        });

//        budgetAmount.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode == KeyEvent.KEYCODE_DEL) {
////                    budgetAmount.setText("");
//                    Log.i("applog", "delete pressed");
//                }
//                return false;
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Start up dialog
                //Based on dialog selection, update listItems
                final ListView monthList = new ListView(getContext());
                monthAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_multiple_choice, months);
                monthList.setAdapter(monthAdapter);
                monthList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_delete)
                        .setTitle("Select month(s) to apply budget to")
                        .setView(monthList)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getContext(), "Save selected", Toast.LENGTH_LONG).show();
                                listItems.clear();
                                SparseBooleanArray selectedMonths = monthList.getCheckedItemPositions();
                                int monthsPerLine = 0;
                                String row = "";
                                for (int i = 0; i < 12; i++) {
                                    if (selectedMonths.get(i)) {
                                        if (monthsPerLine < 3) {
//                                            monthsPerLine++;
//                                            row = row + months.get(i) + " ";
                                            listItems.add(months.get(i));
                                            Log.i("applog", "Row is " + row);
                                        }
//                                        else {
//                                            monthsPerLine = 0;
//                                            listItems.add(row);
//                                            row = "";
//                                        }
                                    }
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null);

                dialog.show();
            }
        });


        amountSeekBar.setMax(1000);
        amountSeekBar.setProgress(500);
        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                budgetAmount.setText(String.valueOf(progress));
                amount = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
