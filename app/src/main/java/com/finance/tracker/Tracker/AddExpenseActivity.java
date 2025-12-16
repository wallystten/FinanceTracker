package com.finance.tracker;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        EditText edtDescription = findViewById(R.id.edtDescription);
        EditText edtValue = findViewById(R.id.edtValue);
        EditText edtBank = findViewById(R.id.edtBank);
        Button btnSave = findViewById(R.id.btnSave);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {

            String description = edtDescription.getText().toString();
            String valueText = edtValue.getText().toString();
            String bank = edtBank.getText().toString();

            if (description.isEmpty() || valueText.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            double value = Double.parseDouble(valueText);

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("description", description);
            values.put("value", value);
            values.put("bank", bank);
            values.put("date", System.currentTimeMillis());

            db.insert(DatabaseHelper.TABLE_EXPENSES, null, values);

            Toast.makeText(this, "Gasto salvo com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
