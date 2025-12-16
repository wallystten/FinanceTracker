package com.finance.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v ->
                Toast.makeText(this, "Função em desenvolvimento", Toast.LENGTH_SHORT).show()
        );
    }
}
