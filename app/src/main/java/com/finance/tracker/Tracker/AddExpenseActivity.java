package com.finance.tracker;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Spinner spCategoria = findViewById(R.id.spCategoria);
        Spinner spBanco = findViewById(R.id.spBanco);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        // Categorias simples
        String[] categorias = {
                "Alimentação", "Transporte", "Moradia",
                "Lazer", "Saúde", "Outros"
        };

        // Bancos e fintechs conhecidos
        String[] bancos = {
                "Dinheiro",
                "Nubank",
                "Banco do Brasil",
                "Caixa",
                "Bradesco",
                "Itaú",
                "Santander",
                "Inter",
                "PicPay",
                "Mercado Pago",
                "PagBank",
                "Outro"
        };

        spCategoria.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categorias
        ));

        spBanco.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                bancos
        ));

        btnSalvar.setOnClickListener(v ->
                Toast.makeText(this,
                        "Gasto salvo (simulação)",
                        Toast.LENGTH_SHORT).show()
        );
    }
}
