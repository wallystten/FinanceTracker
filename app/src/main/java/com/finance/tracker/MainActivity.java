package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtStatus = findViewById(R.id.txtStatus);

        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        Button btnAddIncome = findViewById(R.id.btnAddIncome);

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });

        btnAddIncome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            intent.putExtra("type", "income");
            startActivity(intent);
        });

        txtStatus.setText("App pronto para uso.");
    }
}
