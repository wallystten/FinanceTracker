package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private DatabaseHelper db;
    private TextView txtSaldo;
    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 32, 32, 32);

        txtSaldo = new TextView(this);
        txtSaldo.setTextSize(26);
        txtSaldo.setGravity(Gravity.CENTER);
        root.addView(txtSaldo);

        Button btnQr = new Button(this);
        btnQr.setText("📷 Ler nota fiscal (QR Code)");
        root.addView(btnQr);

        listContainer = new LinearLayout(this);
        listContainer.setOrientation(LinearLayout.VERTICAL);
        root.addView(listContainer);

        setContentView(root);

        btnQr.setOnClickListener(v -> {
            startActivity(new Intent(this, QrScanActivity.class));
        });

        atualizarTela();
    }

    private void atualizarTela() {

        double saldo = db.getBalance();
        txtSaldo.setText("Saldo\nR$ " + String.format("%.2f", saldo));
        txtSaldo.setTextColor(saldo >= 0 ? Color.GREEN : Color.RED);

        listContainer.removeAllViews();

        List<String[]> list = db.getAllTransactionsDetailed();

        for (String[] item : list) {

            TextView tv = new TextView(this);
            tv.setText(
                    "R$ " + item[0] + "\n" +
                            item[2] + "\n" +
                            item[3]
            );
            tv.setPadding(24, 24, 24, 24);
            tv.setBackgroundColor(Color.WHITE);

            if (item[4] != null && !item[4].isEmpty()) {
                tv.setOnClickListener(v -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(item[4])
                            )
                    );
                });
            }

            listContainer.addView(tv);
        }
    }
}
