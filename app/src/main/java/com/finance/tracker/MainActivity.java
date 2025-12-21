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
    private static final int REQUEST_QR = 2;

    private DatabaseHelper db;
    private TextView txtSaldo;
    private LinearLayout listContainer;
    private Button btnQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

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
        txtSaldo.setTextSize(24);
        txtSaldo.setGravity(Gravity.CENTER);

        cardSaldo.addView(txtSaldo);
        root.addView(cardSaldo);

        // ===== BOTÕES =====
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

        LinearLayout.LayoutParams btnParams =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        btnParams.setMargins(0, 0, 16, 0);
        btnGasto.setLayoutParams(btnParams);

        LinearLayout.LayoutParams btnParams2 =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        btnReceita.setLayoutParams(btnParams2);

        row.addView(btnGasto);
        row.addView(btnReceita);
        root.addView(row);

        // ===== BOTÃO QR CODE =====
        btnQr = new Button(this);
        btnQr.setText("📷 LER NOTA FISCAL (QR CODE)");
        btnQr.setBackgroundColor(Color.parseColor("#1976D2"));
        btnQr.setTextColor(Color.WHITE);
        root.addView(btnQr);

        // ===== TÍTULO HISTÓRICO =====
        TextView title = new TextView(this);
        title.setText("Histórico");
        title.setTextSize(18);
        title.setPadding(0, 32, 0, 16);
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
            // FEEDBACK VISUAL
            btnQr.setText("⏳ Aguardando leitura do QR Code...");
            btnQr.setBackgroundColor(Color.parseColor("#9E9E9E"));
            btnQr.setEnabled(false);

            Intent i = new Intent(this, QrScanActivity.class);
            startActivityForResult(i, REQUEST_QR);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ===== VOLTA DO QR =====
        if (requestCode == REQUEST_QR) {
            btnQr.setText("📷 LER NOTA FISCAL (QR CODE)");
            btnQr.setBackgroundColor(Color.parseColor("#1976D2"));
            btnQr.setEnabled(true);
        }

        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK && data != null) {
            double value = data.getDoubleExtra("value", 0);
            String type = data.getStringExtra("type");
            String bank = data.getStringExtra("bank");
            String category = data.getStringExtra("category");

            db.addTransaction(value, type, bank, category);
            atualizarTela();
        }
    }

    private void atualizarTela() {
        double saldo = db.getBalance();
        txtSaldo.setText("Saldo\nR$ " + String.format("%.2f", saldo));
        txtSaldo.setTextColor(saldo >= 0 ? Color.parseColor("#2E7D32") : Color.RED);

        listContainer.removeAllViews();
        List<String> list = db.getAllTransactions();

        if (list.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText(
                    "Nenhuma transação registrada\n\n" +
                    "Use ➖ GASTO, ➕ RECEITA\n" +
                    "ou 📷 QR Code"
            );
            empty.setGravity(Gravity.CENTER);
            empty.setTextSize(16);
            empty.setTextColor(Color.parseColor("#777777"));
            empty.setPadding(0, 80, 0, 0);

            listContainer.addView(empty);
            return;
        }

        for (String item : list) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(32, 24, 32, 24);
            card.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 0, 0, 20);
            card.setLayoutParams(cardParams);

            TextView tv = new TextView(this);
            tv.setText(item);
            tv.setTextSize(16);

            if (item.toLowerCase().contains("gasto")) {
                tv.setTextColor(Color.parseColor("#D32F2F"));
            } else if (item.toLowerCase().contains("receita")) {
                tv.setTextColor(Color.parseColor("#388E3C"));
            }

            card.addView(tv);
            listContainer.addView(card);
        }
    }
}
