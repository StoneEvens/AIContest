package com.AIExpense.elementtest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExpenseDB";
    private static final String TABLE_NAME = "Expense";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INT PRIMARY KEY NOT NULL, TIME DATE NOT NULL, CATEGORY TEXT NOT NULL, NAME TEXT NOT NULL, COST INT NOT NULL)", TABLE_NAME);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addExpense(String time, String category, String name, int cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("TIME", time);
        values.put("CATEGORY", category);
        values.put("NAME", name);
        values.put("COST", cost);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String[]> readExpense(String category, String startTime, String endTime) {
        ArrayList<String[]> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT TIME, NAME, COST FROM %s WHERE CATEGORY = '%s' AND TIME >= '%s' AND TIME <= '%s'", TABLE_NAME, category, startTime, endTime);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String[] expense = new String[3];
                expense[0] = cursor.getString(0);
                expense[1] = cursor.getString(1);
                expense[2] = cursor.getString(2);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return expenseList;
    }
}
