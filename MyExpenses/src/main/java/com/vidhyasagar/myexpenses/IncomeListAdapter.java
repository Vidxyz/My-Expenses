package com.vidhyasagar.myexpenses;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidhyasagar on 5/1/2016.
 */
public class IncomeListAdapter extends ArrayAdapter{

    Context context;
    int resource;
    ArrayList<String> items = null;
    TextView textView;
    ImageView imageView;
    Spinner spinner;
    ArrayAdapter adapter;

    public void disableKeyboard(View v) {
        InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(v.getWindowToken(), 0); //0 is for no custom flags
    }

    public IncomeListAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.items = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // Get the data item for this position
        String field = items.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(position == 0) {
                row = inflater.inflate(R.layout.layout_expense_form_1, parent, false);
            }
            else {
                row = inflater.inflate(R.layout.layout_expense_form_2, parent, false);
            }
        }

        textView = (TextView) row.findViewById(R.id.textField);
        imageView = (ImageView) row.findViewById(R.id.expenseIcon);
        textView.setText(field);

        if(position == 0) {
            EditText editText = (EditText) row.findViewById(R.id.expenseLocation);
            imageView.setImageResource(R.drawable.ic_location);
            editText.setSingleLine(true);
            final View finalRow = row;
            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        disableKeyboard(finalRow);
                    }
                    return false;
                }
            });

        }

        else if(position == 1) {
            imageView.setImageResource(R.drawable.ic_food);
            spinner = (Spinner) row.findViewById(R.id.spinner);
            adapter = ArrayAdapter.createFromResource(getContext(), R.array.expense_categories, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


        }

        else if(position == 2) {
            imageView.setImageResource(R.drawable.ic_debit);
            spinner = (Spinner) row.findViewById(R.id.spinner);
            adapter = ArrayAdapter.createFromResource(getContext(), R.array.expense_types, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

        }

        return row;

    }

}
