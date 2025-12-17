package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int REQUEST_ADD_EXPENSE = 1;
    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddExpense = findViewById(R.id.btnTest);
        listContainer = findViewById(R.id.listContainer);

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(intent, REQUEST_ADD_EXPENSE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_EXPENSE && resultCode == RESULT_OK && data != null) {
            String value = data.getStringExtra("expense_value");

            if (value != null && !value.isEmpty()) {
                TextView item = new TextView(this);
item.setText("R$ " + value);
item.setTextSize(16);
item.setTextColor(0xFF1E1E1E);
item.setPadding(24, 24, 24, 24);
item.setBackgroundColor(0xFFFFFFFF);

LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
params.setMargins(0, 8, 0, 8);
item.setLayoutParams(params);

listContainer.addView(item);
            }
        }
    }
}
