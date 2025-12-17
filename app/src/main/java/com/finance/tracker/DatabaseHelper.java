package com.finance.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "financas.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE = "transactions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "value REAL," +
                        "type TEXT," +
                        "bank TEXT," +
                        "category TEXT," +
                        "date INTEGER" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // Salvar transação COM categoria
    public void addTransaction(double value, String type, String bank, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("value", value);
        cv.put("type", type);
        cv.put("bank", bank);
        cv.put("category", category);
        cv.put("date", System.currentTimeMillis());
        db.insert(TABLE, null, cv);
        db.close();
    }

    // Compatibilidade
    public void addTransaction(double value, String type, String bank) {
        addTransaction(value, type, bank, "Outros");
    }

    public double getBalance() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT SUM(CASE WHEN type='income' THEN value ELSE -value END) FROM " + TABLE,
                null
        );
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        db.close();
        return total;
    }

    // Lista com banco + categoria
    public List<String> getAllTransactions() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT value, type, bank, category FROM " + TABLE + " ORDER BY date DESC",
                null
        );

        while (c.moveToNext()) {
            double value = c.getDouble(0);
            String type = c.getString(1);
            String bank = c.getString(2);
            String category = c.getString(3);

            String line =
                    (type.equals("income") ? "➕ " : "➖ ") +
                            "R$ " + value +
                            " • " + bank +
                            " • " + category;

            list.add(line);
        }

        c.close();
        db.close();
        return list;
    }

    // ✅ Total gasto por categoria (somente despesas)
    public Map<String, Double> getExpensesByCategory() {
        Map<String, Double> map = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT category, SUM(value) " +
                        "FROM " + TABLE +
                        " WHERE type = 'expense' " +
                        "GROUP BY category",
                null
        );

        while (c.moveToNext()) {
            String category = c.getString(0);
            double total = c.getDouble(1);
            map.put(category, total);
        }

        c.close();
        db.close();
        return map;
    }
}
