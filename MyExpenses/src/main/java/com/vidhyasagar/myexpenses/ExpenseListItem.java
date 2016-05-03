package com.vidhyasagar.myexpenses;

import android.widget.ArrayAdapter;

/**
 * Created by Vidhyasagar on 4/27/2016.
 */
public class ExpenseListItem {
    public String expenseLocation;
    public String expenseTime;
    public String expenseType;
    public String expenseIcon;
    public float expenseAmount;

    public ExpenseListItem(){
        super();
    }

    public ExpenseListItem(float expenseAmount, String expenseLocation, String expenseTime, String expenseIcon, String expenseType) {
        super();
        this.expenseAmount = expenseAmount;
        this.expenseLocation = expenseLocation;
        this.expenseTime = expenseTime;
        this.expenseIcon = expenseIcon;
        this.expenseType = expenseType;
    }

}