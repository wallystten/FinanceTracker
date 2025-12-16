package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        EditText edtValue = new EditText(this);
        edtValue.setHint("Valor do gasto (ex: 25.50)");

        Button btnSave = new Button(this);
        btnSave.setText("Salvar gasto");

        layout.addView(edtValue);
        layout.addView(btnSave);

        setContentView(layout);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = edtValue.getText().toString();

                Intent result = new Intent();
                result.putExtra("expense_value", value);

                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }
}
