package com.vidhyasagar.myexpenses;

import android.content.Context;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

    //Variables to save an expense
    String expAmount;
    String expLocation;
    String expTime;
    String expType;
    String expMethod;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        //Date stuff
        todaysDate = (TextView) x.findViewById(R.id.todaysDate);
        c = Calendar.getInstance();

        //Form time format later on when the save button is clicked
        final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

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


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save objects now
                expAmount = amount.getText().toString();

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

            }
        });

        return x;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
