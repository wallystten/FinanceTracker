package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NfceAddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Adicionar gasto da NFC-e");
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        EditText edtValor = new EditText(this);
        edtValor.setHint("Valor (ex: 125.80)");
        root.addView(edtValor);

        EditText edtCategoria = new EditText(this);
        edtCategoria.setHint("Categoria (ex: Combustível)");
        root.addView(edtCategoria);

        Button btnSalvar = new Button(this);
        btnSalvar.setText("Salvar gasto");
        btnSalvar.setBackgroundColor(Color.parseColor("#1976D2"));
        btnSalvar.setTextColor(Color.WHITE);
        root.addView(btnSalvar);

        setContentView(root);

        btnSalvar.setOnClickListener(v -> {
            double valor = 0;
            try {
                valor = Double.parseDouble(edtValor.getText().toString());
            } catch (Exception ignored) {}

            Intent result = new Intent();
            result.putExtra("value", valor);
            result.putExtra("type", "expense");
            result.putExtra("bank", "NFC-e");
            result.putExtra("category", edtCategoria.getText().toString());

            setResult(RESULT_OK, result);
            finish();
        });
    }
}
