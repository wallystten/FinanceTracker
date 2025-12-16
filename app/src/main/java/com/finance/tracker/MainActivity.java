package com.finance.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtStatus = findViewById(R.id.txtStatus);
        Button btnTest = findViewById(R.id.btnTest);

        btnTest.setOnClickListener(v ->
                txtStatus.setText("Botão funcionando 👍")
        );
    }
}
