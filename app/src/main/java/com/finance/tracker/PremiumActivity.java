package com.finance.tracker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PremiumActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("💎 Versão Premium");
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 32);
        root.addView(title);

        TextView benefits = new TextView(this);
        benefits.setText(
                "Com o Premium você desbloqueia:\n\n" +
                "📷 Leitura de nota fiscal (QR Code)\n" +
                "📊 Relatórios por categoria\n" +
                "🏦 Integração com bancos\n" +
                "☁️ Backup na nuvem\n" +
                "🚀 Atualizações futuras\n"
        );
        benefits.setTextSize(16);
        benefits.setPadding(0, 0, 0, 40);
        root.addView(benefits);

        Button btnUpgrade = new Button(this);
        btnUpgrade.setText("Ativar Premium");
        btnUpgrade.setBackgroundColor(Color.parseColor("#4CAF50"));
        btnUpgrade.setTextColor(Color.WHITE);
        root.addView(btnUpgrade);

        setContentView(root);

        btnUpgrade.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Pagamento será ativado em breve",
                    Toast.LENGTH_LONG
            ).show();
        });
    }
}
