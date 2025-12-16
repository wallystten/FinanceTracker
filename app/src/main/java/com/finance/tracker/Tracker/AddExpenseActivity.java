package com.finance.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
        Button btnSave = findViewById(R.id.btnSaveExpense);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = edtDescription.getText().toString();
                String value = edtValue.getText().toString();

                Toast.makeText(
                        AddExpenseActivity.this,
                        "Gasto salvo: " + desc + " - R$ " + value,
                        Toast.LENGTH_SHORT
                ).show();

                finish(); // volta para a tela principal
            }
        });
    }
}
