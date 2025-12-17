package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String type = getIntent().getStringExtra("type");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText(type.equals("income") ? "Adicionar Receita" : "Adicionar Gasto");
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(title);

        EditText edtValue = new EditText(this);
        edtValue.setHint("Valor (ex: 25.50)");
        edtValue.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        root.addView(edtValue);

        // Spinner de bancos
        Spinner spinnerBank = new Spinner(this);
        String[] banks = new String[]{
                "Nubank",
                "Itaú",
                "Bradesco",
                "Santander",
                "Inter",
                "C6 Bank",
                "Caixa",
                "Banco do Brasil",
                "PicPay",
                "Mercado Pago",
                "Pix",
                "Dinheiro",
                "Outro"
        };
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, banks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(adapter);
        root.addView(spinnerBank);

        Button btnSalvar = new Button(this);
        btnSalvar.setText("Salvar");
        root.addView(btnSalvar);

        setContentView(root);

        btnSalvar.setOnClickListener(v -> {
            double value = 0;
            try {
                value = Double.parseDouble(edtValue.getText().toString());
            } catch (Exception ignored) {}

            String bank = spinnerBank.getSelectedItem().toString();

            Intent result = new Intent();
            result.putExtra("value", value);
            result.putExtra("type", type);
            result.putExtra("bank", bank);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
