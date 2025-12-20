package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NfceAddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfce_add);

        EditText edtValor = findViewById(R.id.edtValor);
        EditText edtCategoria = findViewById(R.id.edtCategoria);
        EditText edtPagamento = findViewById(R.id.edtPagamento);
        TextView txtChave = findViewById(R.id.txtChave);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        String chave = getIntent().getStringExtra("chave_nfce");
        if (chave != null) {
            txtChave.setText("Chave NFC-e:\n" + chave);
        }

        btnSalvar.setOnClickListener(v -> {
            if (edtValor.getText().toString().isEmpty()) {
                Toast.makeText(this, "Informe o valor", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();
            data.putExtra("value", Double.parseDouble(edtValor.getText().toString()));
            data.putExtra("type", "expense");
            data.putExtra("bank", edtPagamento.getText().toString());
            data.putExtra("category", edtCategoria.getText().toString());

            setResult(RESULT_OK, data);
            finish();
        });
    }
}
