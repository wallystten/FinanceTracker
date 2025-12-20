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
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(Color.parseColor("#F2F2F2"));

        // TÍTULO
        TextView title = new TextView(this);
        title.setText("Confirmar Nota Fiscal");
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 40);
        root.addView(title);

        // VALOR
        EditText edtValor = new EditText(this);
        edtValor.setHint("Valor total da nota (R$)");
        edtValor.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        root.addView(edtValor);

        // CATEGORIA
        EditText edtCategoria = new EditText(this);
        edtCategoria.setHint("Categoria (ex: Supermercado)");
        edtCategoria.setPadding(0, 32, 0, 32);
        root.addView(edtCategoria);

        // BOTÃO SALVAR
        Button btnSalvar = new Button(this);
        btnSalvar.setText("Salvar gasto");
        btnSalvar.setBackgroundColor(Color.parseColor("#1976D2"));
        btnSalvar.setTextColor(Color.WHITE);
        root.addView(btnSalvar);

        setContentView(root);

        btnSalvar.setOnClickListener(v -> {
            String valorStr = edtValor.getText().toString().trim();
            String categoria = edtCategoria.getText().toString().trim();

            if (valorStr.isEmpty()) {
                edtValor.setError("Informe o valor");
                return;
            }

            double valor = Double.parseDouble(valorStr);

            Intent data = new Intent();
            data.putExtra("value", valor);
            data.putExtra("type", "expense");
            data.putExtra("bank", "NFC-e");
            data.putExtra("category", categoria.isEmpty() ? "Nota Fiscal" : categoria);

            setResult(RESULT_OK, data);
            finish();
        });
    }
}
