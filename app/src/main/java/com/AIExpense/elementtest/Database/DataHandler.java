package com.AIExpense.elementtest.Database;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

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

            Log.e("Check", date + " " + category + " " + item + " " + price);

            dbHandler.addExpense(date, category, item, Integer.parseInt(price));
        }
    }

    public ArrayList<String> getExpense(String category, String month) {
        ArrayList<String[]> dataList = dbHandler.readExpense(category, month);
        ArrayList<String> records = new ArrayList<>();

        for (String[] s: dataList) {
            StringBuilder record = new StringBuilder();

            for (String str: s) {
                record.append(str + ",");
            }

            records.add(record.toString());
            Log.e("Test", record.toString());
        }

        return records;
    }

}
