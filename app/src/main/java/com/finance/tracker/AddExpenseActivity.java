package com.finance.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout simples para evitar crashes
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("Adicionar Gasto");
        title.setTextSize(22);

        TextView subtitle = new TextView(this);
        subtitle.setText("Tela estável. Próximo passo: formulário.");
        subtitle.setTextSize(16);

        layout.addView(title);
        layout.addView(subtitle);

        setContentView(layout);
    }
}
