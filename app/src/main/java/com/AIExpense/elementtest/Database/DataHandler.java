package com.AIExpense.elementtest.Database;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

public class DataHandler {
    private final DBHandler dbHandler;

    public DataHandler(Context context) {
        dbHandler = new DBHandler(context);
    }

    public void saveExpenses(String expenses) {
        ArrayList<String> expenseList = new ArrayList<>();
        expenseList.addAll(Arrays.asList(expenses.split("\n")));

        for (String s: expenseList) {
            String[] record = s.split("]:");
            String date = record[0].substring(1);
            String category = record[1].substring(1);
            String item = record[2].substring(1);
            String price = record[3].substring(1, record[3].length() - 1);

            dbHandler.addExpense(date, category, item, Integer.parseInt(price));
        }
    }

}
