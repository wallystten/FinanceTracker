package com.finance.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "financas.db";
    private static final int DATABASE_VERSION = 4; // ⬅️ aumentou

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
                        "note_url TEXT," + // ⬅️ NOVO
                        "date INTEGER" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // Salvar transação COM nota fiscal
    public void addTransaction(double value, String type, String bank, String category, String noteUrl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("value", value);
        cv.put("type", type);
        cv.put("bank", bank);
        cv.put("category", category);
        cv.put("note_url", noteUrl);
        cv.put("date", System.currentTimeMillis());
        db.insert(TABLE, null, cv);
        db.close();
    }

    // Compatibilidade antiga
    public void addTransaction(double value, String type, String bank, String category) {
        addTransaction(value, type, bank, category, null);
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

    public List<String> getAllTransactions() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT value, type, bank, category, note_url FROM " + TABLE + " ORDER BY date DESC",
                null
        );

        while (c.moveToNext()) {
            double value = c.getDouble(0);
            String type = c.getString(1);
            String bank = c.getString(2);
            String category = c.getString(3);
            String noteUrl = c.getString(4);

            String line =
                    (type.equals("income") ? "➕ " : "➖ ") +
                    "R$ " + value +
                    " • " + bank +
                    " • " + category;

            if (noteUrl != null) {
                line += "\n📄 Nota fiscal salva";
            }

            list.add(line);
        }

        c.close();
        db.close();
        return list;
    }
}
