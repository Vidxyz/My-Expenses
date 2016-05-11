package com.vidhyasagar.myexpenses;

/**
 * Created by Vidhyasagar on 5/11/2016.
 */
public class DialogListItem {
    String location;
    String amount;
    String date;
    String method;

    DialogListItem(String location, String amount, String date, String method) {
        this.location = location;
        this.amount = amount;
        this.date = date;
        this.method = method;
    }

    public String getLocation() {
        return this.location;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getDate() {
        return this.date;
    }

    public String getMethod() {
        return this.method;
    }
}
