package com.vidhyasagar.myexpenses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidhyasagar on 5/6/2016.
 */
public class BudgetListAdapter extends ArraySwipeAdapter {

    Context context;
    int resource;
    ArrayList<BudgetObject> budgets = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    public BudgetListAdapter(Context context, int resource, ArrayList<BudgetObject> budgets, FragmentManager fragmentManager) {
        super(context, resource, budgets);
        this.context = context;
        this.resource = resource;
        this.budgets = budgets;
        this.fragmentManager = fragmentManager;
    }

    public void setupEditEntry(View view, final int position) {

        ImageView editButton = (ImageView) view.findViewById(R.id.listEditButton);
        editButton.setClickable(true);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Edit Budget for the month of " + budgets.get(position).getMonth())
                        .setIcon(R.drawable.ic_edit)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                budgets.get(position).setMonth("PIKACHU");
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null);

                dialog.show();
            }
        });
    }



    public void setupDeleteEntry(View view, final int position) {
        ImageView deleteButton = (ImageView) view.findViewById(R.id.listDeleteButton);
        deleteButton.setClickable(true);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change budget pertaining to this month to UNSET
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Confirmation required")
                        .setIcon(R.drawable.ic_edit)
                        .setMessage("Are you sure you want to remove the set budget for the month of " + budgets.get(position).getMonth() )
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                budgets.get(position).setAmount("UNSET");
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null);

                dialog.show();
            }
        });
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.budgetSwipeLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
            BudgetObject object = budgets.get(position);

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_list_budget, parent, false);
            }

            TextView budgetMonth = (TextView) row.findViewById(R.id.budgetMonth);
            TextView budgetAmount = (TextView) row.findViewById(R.id.monthlyBudget);
            ImageView budgetIcon = (ImageView) row.findViewById(R.id.monthIcon);

            if(budgetAmount != null && budgetMonth != null && budgetIcon != null) {
                if(!object.getAmount().equals("UNSET")) {
                    budgetAmount.setText("$ " + object.getAmount());
                }
                else {
                    budgetAmount.setText(object.getAmount());
                }
                budgetMonth.setText(object.getMonth());
                budgetIcon.setImageResource(R.drawable.ic_receive);
            }

        setupEditEntry(row, position);
        setupDeleteEntry(row, position);

        return row;

    }
}
