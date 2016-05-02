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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
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
    ListView listView;
    IncomeListAdapter adapter;
    ArrayList<String> options;
    Button cancelButton;
    Button saveButton;

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

        options = new ArrayList<>();
        options.add("Expense Location");
        options.add("Expense Category");
        options.add("Expense Type");

        listView = (ListView) x.findViewById(R.id.listView);
        adapter = new IncomeListAdapter(getActivity(), R.layout.layout_expense_form_1, options);
        listView.setAdapter(adapter);

        expLocation = ((EditText) x.findViewById(R.id.expenseLocation)).getText().toString();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save objects now
                expAmount = amount.getText().toString();

//                Log.i("applog", "Amount : " + expAmount);
//                Log.i("applog", "Location : " + adapter.getItem(0));
//                Log.i("applog", "Type : " + adapter.getItem(1));
//                Log.i("applog", "Method : " + adapter.getItem(2));
//                Log.i("applog", "Time : " + c.getTime());
//
                ParseObject newExpense = new ParseObject("Expenses");
//                newExpense.put("amount", Float.parseFloat(expAmount));
//                newExpense.put("time", c.getTime());
//                newExpense.p ut("category", adapter.getItem(1));
//                newExpense.put("method", adapter.getItem(2));
//                newExpense.put("location", expLocation);

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
