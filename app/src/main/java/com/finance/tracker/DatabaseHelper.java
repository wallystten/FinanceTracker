package com.finance.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "financas.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_TRANSACTIONS = "transactions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "value REAL, " +
                        "type TEXT, " +        // income ou expense
                        "date INTEGER" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // Salva gasto ou receita
    public void addTransaction(double value, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("value", value);
        values.put("type", type);
        values.put("date", System.currentTimeMillis());

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    // Calcula saldo automaticamente
    public double getBalance() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(CASE " +
                        "WHEN type='income' THEN value " +
                        "WHEN type='expense' THEN -value " +
                        "ELSE 0 END) FROM " + TABLE_TRANSACTIONS,
                null
        );

        double balance = 0;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return balance;
    }
}
