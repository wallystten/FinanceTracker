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
                        "status TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(db);
    }

    // ✅ MÉTODO PADRÃO (AGORA COM STATUS)
    public void addTransaction(
            double value,
            String type,
            String bank,
            String category,
            String status
    ) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("value", value);
        cv.put("type", type);
        cv.put("bank", bank);
        cv.put("category", category);
        cv.put("status", status);

        db.insert("transactions", null, cv);
    }

    public double getBalance() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT value, type FROM transactions", null);

        double balance = 0;

        while (c.moveToNext()) {
            double value = c.getDouble(0);
            String type = c.getString(1);

            if ("income".equals(type)) {
                balance += value;
            } else {
                balance -= value;
            }
        }

        c.close();
        return balance;
    }

    public List<String> getAllTransactions() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT value, bank, category, status FROM transactions ORDER BY id DESC",
                null
        );

        while (c.moveToNext()) {
            double value = c.getDouble(0);
            String bank = c.getString(1);
            String category = c.getString(2);
            String status = c.getString(3);

            if ("pendente".equals(status)) {
                list.add("Nota fiscal (pendente) - " + bank);
            } else {
                list.add("R$ " + value + " - " + bank + " (" + category + ")");
            }
        }

        c.close();
        return list;
    }
}
public List<String[]> getAllTransactionsDetailed() {
    List<String[]> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();

    Cursor c = db.rawQuery(
            "SELECT value, type, bank, category, status FROM transactions ORDER BY id DESC",
            null
    );

    while (c.moveToNext()) {
        String[] item = new String[5];

        item[0] = String.valueOf(c.getDouble(0)); // value
        item[1] = c.getString(1);                 // type
        item[2] = c.getString(2);                 // bank
        item[3] = c.getString(3);                 // category
        item[4] = c.getString(4);                 // status

        list.add(item);
    }

    c.close();
    return list;
}
