package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        root.addView(title);

        EditText edtValue = new EditText(this);
        edtValue.setHint("Valor (ex: 25.50)");
        edtValue.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        root.addView(edtValue);

        Button btnSalvar = new Button(this);
        btnSalvar.setText("Salvar");
        root.addView(btnSalvar);

        setContentView(root);

        btnSalvar.setOnClickListener(v -> {
            double value = 0;
            try {
                value = Double.parseDouble(edtValue.getText().toString());
            } catch (Exception ignored) {}

            Intent result = new Intent();
            result.putExtra("value", value);
            result.putExtra("type", type);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
