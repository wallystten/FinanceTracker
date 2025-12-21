package com.finance.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "finance.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE transactions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "value REAL," +
                        "type TEXT," +
                        "bank TEXT," +
                        "category TEXT," +
                        "note_link TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(db);
    }

    public void addTransaction(
            double value,
            String type,
            String bank,
            String category,
            String noteLink
    ) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("value", value);
        cv.put("type", type);
        cv.put("bank", bank);
        cv.put("category", category);
        cv.put("note_link", noteLink);

        db.insert("transactions", null, cv);
    }

    public double getBalance() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT SUM(CASE WHEN type='income' THEN value ELSE -value END) FROM transactions",
                null
        );

        double total = 0;
        if (c.moveToFirst()) {
            total = c.getDouble(0);
        }
        c.close();
        return total;
    }

    public List<String[]> getAllTransactionsDetailed() {

        List<String[]> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT value, type, bank, category, note_link FROM transactions ORDER BY id DESC",
                null
        );

        while (c.moveToNext()) {

            String[] item = new String[5];
            item[0] = c.getString(0);
            item[1] = c.getString(1);
            item[2] = c.getString(2);
            item[3] = c.getString(3);
            item[4] = c.getString(4);

            list.add(item);
        }

        c.close();
        return list;
    }
}
