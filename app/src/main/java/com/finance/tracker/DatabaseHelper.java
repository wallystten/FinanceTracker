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
                        "status TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE transactions ADD COLUMN status TEXT");
        }
    }

    // ===== INSERÇÃO =====
    public void addTransaction(double value, String type, String bank, String category, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("value", value);
        cv.put("type", type);
        cv.put("bank", bank);
        cv.put("category", category);
        cv.put("status", status);

        db.insert("transactions", null, cv);
    }

    // ===== SALDO =====
    public double getBalance() {
        double balance = 0;
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT value, type FROM transactions WHERE status='CONFIRMED'",
                null
        );

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

    // ===== LISTA SIMPLES =====
    public List<String> getAllTransactions() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT value, type, category FROM transactions ORDER BY id DESC",
                null
        );

        while (c.moveToNext()) {
            double value = c.getDouble(0);
            String type = c.getString(1);
            String category = c.getString(2);

            String line = (type.equals("income") ? "+ " : "- ") +
                    "R$ " + String.format("%.2f", value) +
                    " • " + category;

            list.add(line);
        }

        c.close();
        return list;
    }

    // ===== LISTA DETALHADA (🔥 ERA O MÉTODO QUE FALTAVA) =====
    public List<String[]> getAllTransactionsDetailed() {
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT value, type, bank, category, status FROM transactions ORDER BY id DESC",
                null
        );

        while (c.moveToNext()) {
            String[] item = new String[5];

            item[0] = String.valueOf(c.getDouble(0));
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
