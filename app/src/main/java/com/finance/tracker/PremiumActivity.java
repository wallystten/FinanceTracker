package com.finance.tracker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PremiumActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("💎 Plano Premium");
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 32);

        TextView desc = new TextView(this);
        desc.setText(
                "Com o Premium você poderá:\n\n" +
                "✔ Ler notas fiscais automaticamente\n" +
                "✔ Extrair valores da nota\n" +
                "✔ Categorizar gastos automaticamente\n\n" +
                "Pagamento será ativado em breve."
        );
        desc.setTextSize(16);

        root.addView(title);
        root.addView(desc);

        setContentView(root);
    }
}
