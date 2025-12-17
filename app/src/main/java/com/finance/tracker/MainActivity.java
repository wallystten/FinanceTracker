package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int REQUEST_ADD = 1;

    private DatabaseHelper db;
    private TextView txtSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        // Layout principal
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 32, 32, 32);

        // Saldo
        txtSaldo = new TextView(this);
        txtSaldo.setTextSize(22);
        root.addView(txtSaldo);

        // Linha de botões
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        Button btnGasto = new Button(this);
        btnGasto.setText("➖ Gasto");

        Button btnReceita = new Button(this);
        btnReceita.setText("➕ Receita");

        row.addView(btnGasto);
        row.addView(btnReceita);

        root.addView(row);

        setContentView(root);

        atualizarSaldo();

        // Botão gasto
        btnGasto.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.putExtra("type", "expense");
            startActivityForResult(intent, REQUEST_ADD);
        });

        // Botão receita
        btnReceita.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.putExtra("type", "income");
            startActivityForResult(intent, REQUEST_ADD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK && data != null) {
            double value = data.getDoubleExtra("value", 0);
            String type = data.getStringExtra("type");

            db.addTransaction(value, type);
            atualizarSaldo();
        }
    }

    private void atualizarSaldo() {
        double saldo = db.getBalance();
        txtSaldo.setText("Saldo: R$ " + String.format("%.2f", saldo));
    }
}
