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
import java.util.Map;

public class MainActivity extends Activity {

    private static final int REQUEST_ADD = 1;

    private DatabaseHelper db;
    private TextView txtSaldo;
    private LinearLayout summaryCategoryContainer;
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
        cardSaldo.setPadding(48, 48, 48, 48);
        cardSaldo.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        cardParams.setMargins(0, 0, 0, 32);
        cardSaldo.setLayoutParams(cardParams);

        txtSaldo = new TextView(this);
        txtSaldo.setTextSize(26);
        txtSaldo.setGravity(Gravity.CENTER);

        cardSaldo.addView(txtSaldo);
        root.addView(cardSaldo);

        // ===== BOTÕES =====
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 0, 0, 32);

        Button btnGasto = new Button(this);
        btnGasto.setText("➖ Gasto");
        btnGasto.setBackgroundColor(Color.parseColor("#F44336"));
        btnGasto.setTextColor(Color.WHITE);

        Button btnReceita = new Button(this);
        btnReceita.setText("➕ Receita");
        btnReceita.setBackgroundColor(Color.parseColor("#4CAF50"));
        btnReceita.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams btnParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
        btnParams.setMargins(8, 0, 8, 0);
        btnGasto.setLayoutParams(btnParams);
        btnReceita.setLayoutParams(btnParams);

        row.addView(btnGasto);
        row.addView(btnReceita);
        root.addView(row);

        // ===== RESUMO POR CATEGORIA =====
        TextView resumoTitle = new TextView(this);
        resumoTitle.setText("📊 Gastos por categoria");
        resumoTitle.setTextSize(18);
        resumoTitle.setPadding(0, 0, 0, 16);
        root.addView(resumoTitle);

        summaryCategoryContainer = new LinearLayout(this);
        summaryCategoryContainer.setOrientation(LinearLayout.VERTICAL);
        root.addView(summaryCategoryContainer);

        // ===== HISTÓRICO =====
        TextView title = new TextView(this);
        title.setText("Histórico");
        title.setTextSize(18);
        title.setPadding(0, 32, 0, 16);
        root.addView(title);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        // ===== SALDO =====
        double saldo = db.getBalance();
        txtSaldo.setText("Saldo\nR$ " + String.format("%.2f", saldo));
        txtSaldo.setTextColor(saldo >= 0 ? Color.parseColor("#2E7D32") : Color.RED);

        // ===== RESUMO POR CATEGORIA =====
        summaryCategoryContainer.removeAllViews();
        Map<String, Double> resumo = db.getExpensesByCategory();

        for (String categoria : resumo.keySet()) {
            TextView tv = new TextView(this);
            tv.setText(categoria + ": R$ " + String.format("%.2f", resumo.get(categoria)));
            tv.setTextSize(16);
            tv.setPadding(24, 16, 24, 16);
            tv.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams p =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 0, 0, 12);
            tv.setLayoutParams(p);

            summaryCategoryContainer.addView(tv);
        }

        // ===== HISTÓRICO =====
        listContainer.removeAllViews();
        List<String> list = db.getAllTransactions();

        if (list.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Nenhuma transação ainda");
            listContainer.addView(tv);
            return;
        }

        for (String item : list) {
            TextView tv = new TextView(this);
            tv.setText(item);
            tv.setTextSize(16);
            tv.setPadding(24, 24, 24, 24);
            tv.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams p =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 0, 0, 16);
            tv.setLayoutParams(p);

            listContainer.addView(tv);
        }
    }
}
