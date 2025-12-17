package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int REQUEST_ADD = 1;

    private LinearLayout listContainer;
    private TextView txtTotal;
    private double saldo = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTotal = findViewById(R.id.txtTotal);
        listContainer = findViewById(R.id.listContainer);

        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        Button btnAddIncome  = findViewById(R.id.btnAddIncome);

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.putExtra("type", "expense");
            startActivityForResult(intent, REQUEST_ADD);
        });

        btnAddIncome.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.putExtra("type", "income");
            startActivityForResult(intent, REQUEST_ADD);
        });

        atualizarSaldo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK && data != null) {

            double value = data.getDoubleExtra("value", 0);
            String type = data.getStringExtra("type");

            if ("income".equals(type)) {
                saldo += value;
            } else {
                saldo -= value;
            }

            TextView item = new TextView(this);
            item.setText(
                    (type.equals("income") ? "➕ Receita: R$ " : "➖ Gasto: R$ ") + value
            );
            item.setPadding(24, 24, 24, 24);

            listContainer.addView(item);
            atualizarSaldo();
        }
    }

    private void atualizarSaldo() {
        txtTotal.setText(String.format("Saldo: R$ %.2f", saldo));
    }
}
