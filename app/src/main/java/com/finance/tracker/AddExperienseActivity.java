package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String type = getIntent().getStringExtra("type");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        EditText edtValue = new EditText(this);
        edtValue.setHint("Digite o valor");

        Button btnSave = new Button(this);
        btnSave.setText(
                "income".equals(type) ? "Salvar Receita" : "Salvar Gasto"
        );

        layout.addView(edtValue);
        layout.addView(btnSave);

        setContentView(layout);

        btnSave.setOnClickListener(v -> {
            double value = Double.parseDouble(edtValue.getText().toString());

            Intent result = new Intent();
            result.putExtra("value", value);
            result.putExtra("type", type);

            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
}
