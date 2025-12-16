package com.finance.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AddExpenseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText("Tela Adicionar Gasto");
        tv.setTextSize(22);

        setContentView(tv);
    }
}
