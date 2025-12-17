package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define tipo (gasto ou receita) de forma FINAL
        final String transactionType =
                getIntent().getStringExtra("type") != null
                        ? getIntent().getStringExtra("type")
                        : "expense";

        // Layout base
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // Título
        TextView title = new TextView(this);
        title.setText(
                transactionType.equals("income")
                        ? "Adicionar Receita"
                        : "Adicionar Gasto"
        );
        title.setTextSize(22);

        // Campo valor
        EditText edtValue = new EditText(this);
        edtValue.setHint("Valor (ex: 50.00)");
        edtValue.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        // Botão salvar
        Button btnSave = new Button(this);
        btnSave.setText("Salvar");

        layout.addView(title);
        layout.addView(edtValue);
        layout.addView(btnSave);

        setContentView(layout);

        // Ação do botão
        btnSave.setOnClickListener(v -> {
            String valueStr = edtValue.getText().toString().trim();

            if (valueStr.isEmpty()) {
                Toast.makeText(this, "Informe um valor", Toast.LENGTH_SHORT).show();
                return;
            }

            double value = Double.parseDouble(valueStr);

            Intent result = new Intent();
            result.putExtra("value", value);
            result.putExtra("type", transactionType);

            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
}
