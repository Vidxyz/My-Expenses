package com.vidhyasagar.myexpenses;

/**
 * Created by Vidhyasagar on 5/6/2016.
 */
public class BudgetObject {
    String month;
    String amount;

    BudgetObject(String month, String amount) {
        this.month = month;
        this.amount = amount;
    }

    BudgetObject(String month) {
        this.month = month;
    }

    public String getMonth() {
        return this.month;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
