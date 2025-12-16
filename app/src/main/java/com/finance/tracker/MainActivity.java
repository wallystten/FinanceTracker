package com.finance.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtTotal;
    private Button btnAddExpense;
    private Button btnScanQr;
    private Button btnManualImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referências da interface
        txtTotal = findViewById(R.id.txtTotalMonth);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnScanQr = findViewById(R.id.btnScanQr);
        btnManualImport = findViewById(R.id.btnManualImport);

        // Valor inicial (por enquanto fixo)
        txtTotal.setText("R$ 0,00");

        // Botão: Adicionar gasto (manual)
        btnAddExpense.setOnClickListener(v -> {
            Toast.makeText(this, "Adicionar gasto (em breve)", Toast.LENGTH_SHORT).show();
        });

        // Botão: Ler nota fiscal (QR Code)
        btnScanQr.setOnClickListener(v -> {
            Toast.makeText(this, "Leitura de QR Code em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        // Botão: Informar banco / carteira
        btnManualImport.setOnClickListener(v -> {
            Toast.makeText(this, "Banco / carteira em desenvolvimento", Toast.LENGTH_SHORT).show();
        });
    }
}
