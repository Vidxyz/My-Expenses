package com.vidhyasagar.myexpenses;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    Button saveButton;
    Button cancelButton;

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
        budgetAmount = (EditText) getActivity().findViewById(R.id.monthlyBudget);
        amountSeekBar = (SeekBar) getActivity().findViewById(R.id.amountSeekBar);

        //CANCEL AND SAVE BUTTONS
        cancelButton = (Button) getActivity().findViewById(R.id.cancelButton);
        saveButton = (Button) getActivity().findViewById(R.id.saveButton);


        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativeLayout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("applog", "Keyboard should go down now");
                disableKeyboard(v);
            }
        });

        perYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableKeyboard(v);
            }
        });

        perWeek.setOnClickListener(new View.OnClickListener() {
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
                if (!s.toString().equals("")) {
                    perWeek.setText(String.valueOf(Float.parseFloat(s.toString()) / 4) + " / Week");
                    perYear.setText(String.valueOf(Float.parseFloat(s.toString()) * 12) + " / Year");
                } else {
                    perWeek.setText("0 / Week");
                    perYear.setText("0 / Year");
                }
//                amountSeekBar.setProgress((int) Float.parseFloat(s.toString()));
            }
        });


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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("applog", "stopped tracking touch");
            }
        });


        budgetAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    disableKeyboard(v);
                }
                return false;
            }
        });



        //SAVE AND CANCEL BUTTONS
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Budgets");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            if (objects.size() == 0) {
                                //NO object found, must create it
                                Log.i("applog", "Creating new objects");
                                for (String item : listItems) {
                                    ParseObject newObject = new ParseObject("Budgets");
                                    newObject.put("amount", Float.parseFloat(budgetAmount.getText().toString()));
                                    newObject.put("username", ParseUser.getCurrentUser().getUsername());
                                    newObject.put("month", item);
                                    newObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                e.printStackTrace();
                                            } else {
                                                Log.i("applog", "budget object added");
                                            }
                                        }
                                    });
                                }

                                Toast.makeText(getContext(), "Budget Added!", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStackImmediate();
                            } else {
                                for (ParseObject object : objects) {
                                    object.deleteInBackground();
                                }
                                for (String item : listItems) {
                                    ParseObject newObject = new ParseObject("Budgets");
                                    newObject.put("amount", Float.parseFloat(budgetAmount.getText().toString()));
                                    newObject.put("username", ParseUser.getCurrentUser().getUsername());
                                    newObject.put("month", item);
                                    newObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                e.printStackTrace();
                                            } else {
                                                Log.i("applog", "budget object added");
                                            }
                                        }
                                    });
                                }
                            }
                            Toast.makeText(getActivity(), "Budget Updated!", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }
                    }
                });
            }
        });
    }
}
