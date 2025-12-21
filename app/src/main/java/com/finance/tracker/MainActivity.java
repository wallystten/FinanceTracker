package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private static final int REQUEST_ADD = 1;

    private DatabaseHelper db;
    private TextView txtSaldo;
    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        // ===== ROOT =====
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 32, 32, 32);
        root.setBackgroundColor(Color.parseColor("#F2F2F2"));

        // ===== CARD SALDO =====
        LinearLayout cardSaldo = new LinearLayout(this);
        cardSaldo.setOrientation(LinearLayout.VERTICAL);
        cardSaldo.setPadding(40, 40, 40, 40);
        cardSaldo.setBackgroundColor(Color.WHITE);

        txtSaldo = new TextView(this);
        txtSaldo.setTextSize(26);
        txtSaldo.setGravity(Gravity.CENTER);
        txtSaldo.setTextColor(Color.BLACK);

        cardSaldo.addView(txtSaldo);
        root.addView(cardSaldo);

        // ===== BOTÕES GASTO / RECEITA =====
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 40, 0, 40);

        Button btnGasto = new Button(this);
        btnGasto.setText("➖ GASTO");
        btnGasto.setBackgroundColor(Color.parseColor("#F44336"));
        btnGasto.setTextColor(Color.WHITE);

        Button btnReceita = new Button(this);
        btnReceita.setText("➕ RECEITA");
        btnReceita.setBackgroundColor(Color.parseColor("#4CAF50"));
        btnReceita.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        btnGasto.setLayoutParams(p);
        btnReceita.setLayoutParams(p);

        row.addView(btnGasto);
        row.addView(btnReceita);
        root.addView(row);

        // ===== BOTÃO QR CODE =====
        Button btnQr = new Button(this);
        btnQr.setText("📷 LER NOTA FISCAL (QR CODE)");
        btnQr.setBackgroundColor(Color.parseColor("#1976D2"));
        btnQr.setTextColor(Color.WHITE);
        root.addView(btnQr);

        // ===== TÍTULO HISTÓRICO =====
        TextView title = new TextView(this);
        title.setText("Histórico");
        title.setTextSize(18);
        title.setPadding(0, 32, 0, 16);
        title.setTextColor(Color.BLACK);
        root.addView(title);

        // ===== LISTA =====
        listContainer = new LinearLayout(this);
        listContainer.setOrientation(LinearLayout.VERTICAL);
        root.addView(listContainer);

        setContentView(root);

        atualizarTela();

        // ===== AÇÕES =====
        btnGasto.setOnClickListener(v -> {
            Intent i = new Intent(this, AddExpenseActivity.class);
            i.putExtra("type", "expense");
            startActivityForResult(i, REQUEST_ADD);
        });

        btnReceita.setOnClickListener(v -> {
            Intent i = new Intent(this, AddExpenseActivity.class);
            i.putExtra("type", "income");
            startActivityForResult(i, REQUEST_ADD);
        });

        btnQr.setOnClickListener(v -> {
            Intent i = new Intent(this, QrScanActivity.class);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarTela();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK && data != null) {
            double value = data.getDoubleExtra("value", 0);
            String type = data.getStringExtra("type");
            String bank = data.getStringExtra("bank");
            String category = data.getStringExtra("category");

            db.addTransaction(value, type, bank, category, "CONFIRMED");
            atualizarTela();
        }
    }

    private void atualizarTela() {
        double saldo = db.getBalance();

        txtSaldo.setText("Saldo\nR$ " + String.format("%.2f", saldo));
        txtSaldo.setTextColor(
                saldo >= 0
                        ? Color.parseColor("#2E7D32")
                        : Color.RED
        );

        listContainer.removeAllViews();

        List<String> list = db.getAllTransactions();

        if (list.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Nenhuma transação ainda");
            tv.setTextSize(14);
            listContainer.addView(tv);
            return;
        }

        for (String item : list) {
            TextView tv = new TextView(this);
            tv.setText(item);
            tv.setTextSize(16);
            tv.setPadding(24, 24, 24, 24);
            tv.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            lp.setMargins(0, 0, 0, 16);
            tv.setLayoutParams(lp);

            listContainer.addView(tv);
        }
    }
}
