package com.finance.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.content.Intent;
import android.provider.Settings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.widget.Toast;
import android.view.ViewGroup;
import android.graphics.Color;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView tvIncome, tvExpense, tvBalance;
    private LinearLayout container;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this,
                    "Nova transação detectada!",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 40, 40, 40);
        mainLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));

        TextView title = new TextView(this);
        title.setText("💰 Minhas Finanças");
        title.setTextSize(28);
        title.setTextColor(Color.BLACK);
        title.setPadding(0, 0, 0, 40);
        mainLayout.addView(title);

        LinearLayout summaryCard = new LinearLayout(this);
        summaryCard.setOrientation(LinearLayout.HORIZONTAL);
        summaryCard.setPadding(30, 30, 30, 30);
        summaryCard.setBackgroundColor(Color.WHITE);

        LinearLayout incomeBox = createSummaryBox("Ganhos", "R$ 0,00", Color.parseColor("#4CAF50"));
        tvIncome = (TextView) incomeBox.getChildAt(1);
        summaryCard.addView(incomeBox);

        LinearLayout expenseBox = createSummaryBox("Gastos", "R$ 0,00", Color.parseColor("#F44336"));
        tvExpense = (TextView) expenseBox.getChildAt(1);
        summaryCard.addView(expenseBox);

        LinearLayout balanceBox = createSummaryBox("Saldo", "R$ 0,00", Color.parseColor("#2196F3"));
        tvBalance = (TextView) balanceBox.getChildAt(1);
        summaryCard.addView(balanceBox);

        mainLayout.addView(summaryCard);

        Button btnSettings = new Button(this);
        btnSettings.setText("⚙️ Ativar Permissões");
        btnSettings.setTextColor(Color.WHITE);
        btnSettings.setBackgroundColor(Color.parseColor("#FF9800"));
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        });

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        btnParams.setMargins(0, 40, 0, 40);
        btnSettings.setLayoutParams(btnParams);
        mainLayout.addView(btnSettings);

        TextView historyTitle = new TextView(this);
        historyTitle.setText("📋 Histórico");
        historyTitle.setTextSize(18);
        historyTitle.setTextColor(Color.BLACK);
        historyTitle.setPadding(0, 0, 0, 20);
        mainLayout.addView(historyTitle);

        ScrollView scrollView = new ScrollView(this);
        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(container);

        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
        );
        scrollView.setLayoutParams(scrollParams);
        mainLayout.addView(scrollView);

        TextView empty = new TextView(this);
        empty.setText("Nenhuma transação ainda.");
        empty.setTextColor(Color.GRAY);
        empty.setPadding(30, 60, 30, 60);
        container.addView(empty);

        setContentView(mainLayout);

        IntentFilter filter = new IntentFilter("NEW_TRANSACTION");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(receiver, filter);
        }
    }

    private LinearLayout createSummaryBox(String label, String value, int color) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1
        );
        box.setLayoutParams(params);

        TextView labelTv = new TextView(this);
        labelTv.setText(label);
        labelTv.setTextSize(12);
        labelTv.setGravity(android.view.Gravity.CENTER);
        box.addView(labelTv);

        TextView valueTv = new TextView(this);
        valueTv.setText(value);
        valueTv.setTextSize(16);
        valueTv.setTextColor(color);
        valueTv.setGravity(android.view.Gravity.CENTER);
        box.addView(valueTv);

        return box;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
