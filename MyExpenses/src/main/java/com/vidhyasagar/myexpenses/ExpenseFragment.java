package com.vidhyasagar.myexpenses;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Vidhyasagar on 4/30/2016.
 */
public class ExpenseFragment extends Fragment {

    Calendar c;
    TextView todaysDate;
    Button cancelButton;
    Button saveButton;
    Spinner categorySpinner;
    Spinner methodSpinner;
    ArrayAdapter<CharSequence> categoryAdapter;
    ArrayAdapter<CharSequence> methodAdapter;
    Boolean hasArguments = false;
    SharedPreferences sharedPreferences;

    String expAmount;

    public void disableKeyboard(View view) {
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0); //0 is for no custom flags
    }

    public void previousDate(View view) {
        c.setTime(c.getTime());
        c.add(Calendar.DATE, -1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        todaysDate.setText(formattedDate);

    }

    public void nextDate(View view) {
        c.setTime(c.getTime());
        c.add(Calendar.DATE, 1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        todaysDate.setText(formattedDate);
    }

    public int getCategoryPosition() {

        switch(sharedPreferences.getString("category", "")) {
            case "Food": return 0;
            case "Groceries" : return 1;
            case "Movies" : return 2;
            case "Alcohol" : return 3;
            case "Rent" : return 4;
            case "Hydro" : return 5;
            case "Other" : return 6;
        }

        return -1;
    }

    public int getMethodPosition() {
        switch(sharedPreferences.getString("method", "")) {
            case "Debit": return 0;
            case "Cash" : return 1;
            case "Credit" : return 2;
            case "Other" : return 3;
        }

        return -1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        String eLocation = sharedPreferences.getString("location", "");

        if(eLocation.equals("")) {
            Log.i("applog", "bundle is null");
            hasArguments = false;
        }
        else {
            Log.i("applog", "bundle is not null");
            hasArguments = true;
        }



        final View x = inflater.inflate(R.layout.fragment_diary_expense,null);

        //CANCEL AND SAVE BUTTONS
        cancelButton = (Button) x.findViewById(R.id.cancelButton);
        saveButton = (Button) x.findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        //KeyBoard Disablers
        LinearLayout linearLayout = (LinearLayout) x.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableKeyboard(v);
            }
        });

        //Setting maxlines to 1
        final EditText amount = (EditText) x.findViewById(R.id.expenseAmount);
        amount.setSingleLine(true);
        amount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    disableKeyboard(x);
                }
                return false;
            }
        });
        if(hasArguments) {
            amount.setText(String.valueOf(sharedPreferences.getFloat("amount", -1f)));
        }


        final EditText location = (EditText) x.findViewById(R.id.locationEditText);
        location.setSingleLine(true);
        location.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    disableKeyboard(x);
                }
                return false;
            }
        });
        if(hasArguments) {
            location.setText(sharedPreferences.getString("location", ""));
        }

        //Date stuff
        todaysDate = (TextView) x.findViewById(R.id.todaysDate);
        c = Calendar.getInstance();

        //Form time format later on when the save button is clicked
        final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        final SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");

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


        //Main Form
        categorySpinner = (Spinner) x.findViewById(R.id.categorySpinner);
        methodSpinner = (Spinner) x.findViewById(R.id.methodSpinner);

        categoryAdapter = ArrayAdapter.createFromResource(getContext(), R.array.expense_categories, android.R.layout.simple_spinner_dropdown_item);
        methodAdapter = ArrayAdapter.createFromResource(getContext(), R.array.expense_types, android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAdapter);
        methodSpinner.setAdapter(methodAdapter);

        if(hasArguments) {
            int categoryPosition = getCategoryPosition();
            int methodPosition = getMethodPosition();
            categorySpinner.setSelection(categoryPosition);
            methodSpinner.setSelection(methodPosition);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save objects now
                expAmount = amount.getText().toString();

                if (!hasArguments) {

                    Log.i("applog", "Amount : " + expAmount);
                    Log.i("applog", "Location : " + location.getText().toString());
                    Log.i("applog", "Type : " + categorySpinner.getSelectedItem().toString());
                    Log.i("applog", "Method : " + methodSpinner.getSelectedItem().toString());
                    Log.i("applog", "Time : " + timeFormat.format(c.getTime()));
//
                    ParseObject newExpense = new ParseObject("Expenses");
                    newExpense.put("amount", Float.parseFloat(expAmount));
                    newExpense.put("time", c.getTime());
                    newExpense.put("category", categorySpinner.getSelectedItem().toString());
                    newExpense.put("method", methodSpinner.getSelectedItem().toString());
                    newExpense.put("location", location.getText().toString());
                    newExpense.put("username", ParseUser.getCurrentUser().getUsername());

                    newExpense.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "Expense Saved!", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStackImmediate();
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    //Update existing database entry
                    Log.i("applog", "Amount : " + sharedPreferences.getFloat("amount", -1f));
                    Log.i("applog", "Location : " + sharedPreferences.getString("location", ""));
                    Log.i("applog", "Type : " + sharedPreferences.getString("category", ""));
                    Log.i("applog", "Method : " + sharedPreferences.getString("method", ""));
                    Log.i("applog", "Time : " + sharedPreferences.getString("time", "noStringFoundSucka"));

                    final SimpleDateFormat tempDf = new SimpleDateFormat("dd-MMM-yyyy");
                    ParseQuery<ParseObject> pQuery = new ParseQuery<ParseObject>("Expenses");
                    pQuery.whereEqualTo("location", sharedPreferences.getString("location", ""));
                    pQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    pQuery.whereEqualTo("category", sharedPreferences.getString("category", ""));
                    pQuery.whereEqualTo("method", sharedPreferences.getString("method", ""));
                    pQuery.whereEqualTo("amount", sharedPreferences.getFloat("amount", -1f));
                    pQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {

                                if(objects.size() == 0) {
                                    Log.i("applog", "NULL QUERY");
                                }
                                //Scope for improvement, alternate query way or add formatted 'time' for additional accuracy
                                for (ParseObject object : objects) {
                                        object.put("location",location.getText().toString());
                                        object.put("category",categorySpinner.getSelectedItem().toString());
                                        object.put("method",methodSpinner.getSelectedItem().toString());
                                        object.put("amount",Float.parseFloat(expAmount));
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if(e == null) {
                                                    sharedPreferences.edit().clear().commit();
                                                    Toast.makeText(getActivity(), "Expense updated successfully!", Toast.LENGTH_LONG).show();
                                                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                                                }else {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                }
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });


        return x;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
