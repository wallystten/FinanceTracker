package com.finance.tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "transactions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "description TEXT, " +
                "value REAL, " +
                "type TEXT, " +
                "category TEXT, " +
                "timestamp INTEGER)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long addTransaction(Transaction t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("description", t.description);
        values.put("value", t.value);
        values.put("type", t.type);
        values.put("category", t.category);
        values.put("timestamp", t.timestamp);

        long id = db.insert(TABLE, null, values);
        db.close();
        return id;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE,
                null,
                null,
                null,
                null,
                null,
                "timestamp DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction();
                t.id = cursor.getLong(0);
                t.description = cursor.getString(1);
                t.value = cursor.getDouble(2);
                t.type = cursor.getString(3);
                t.category = cursor.getString(4);
                t.timestamp = cursor.getLong(5);
                list.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
