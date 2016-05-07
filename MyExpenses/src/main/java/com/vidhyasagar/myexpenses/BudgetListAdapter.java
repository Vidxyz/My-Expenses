package com.vidhyasagar.myexpenses;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.ArrayList;

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


        return row;

    }
}
