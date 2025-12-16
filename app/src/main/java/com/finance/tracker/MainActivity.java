package com.finance.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText("Minhas Finanças\n\nApp iniciado com sucesso.");
        tv.setTextSize(20);

        setContentView(tv);
    }
}
