package com.vidhyasagar.myexpenses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
public class ViewBudgetFragment extends Fragment {

    ListView budgetList;
    ArrayList<BudgetObject> budgetObjects;
    BudgetListAdapter adapter;

    public void setUpBudgetObjects() {
        budgetObjects.add(new BudgetObject("January", "UNSET"));
        budgetObjects.add(new BudgetObject("February", "UNSET"));
        budgetObjects.add(new BudgetObject("March", "UNSET"));
        budgetObjects.add(new BudgetObject("April", "UNSET"));
        budgetObjects.add(new BudgetObject("May", "UNSET"));
        budgetObjects.add(new BudgetObject("June", "UNSET"));
        budgetObjects.add(new BudgetObject("July", "UNSET"));
        budgetObjects.add(new BudgetObject("August", "UNSET"));
        budgetObjects.add(new BudgetObject("September", "UNSET"));
        budgetObjects.add(new BudgetObject("October", "UNSET"));
        budgetObjects.add(new BudgetObject("November", "UNSET"));
        budgetObjects.add(new BudgetObject("December", "UNSET"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_budget,null);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        budgetObjects = new ArrayList<>();
        setUpBudgetObjects();
        budgetList = (ListView) getActivity().findViewById(R.id.listView2);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Budgets");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null) {
                    e.printStackTrace();
                }
                else {
                    //Special case if query is empty check
                    Log.i("applog - query ", objects.toString());
                    for(ParseObject object : objects) {
                        for(BudgetObject b : budgetObjects) {
                            if(b.getMonth().equals(object.getString("month"))) {
                                b.setAmount(object.getNumber("amount").toString());
                            }
                        }
                    }
                    adapter = new BudgetListAdapter(getContext(), R.layout.layout_list_budget, budgetObjects, getActivity().getSupportFragmentManager());
                    budgetList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
