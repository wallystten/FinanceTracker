package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class QrScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Usa leitor externo (ZXing via Intent)
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Leitor de QR Code não encontrado. Instale um leitor.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String qrContent = data.getStringExtra("SCAN_RESULT");

            if (qrContent != null && qrContent.startsWith("http")) {

                // 🔹 REGISTRA GASTO AUTOMATICAMENTE
                DatabaseHelper db = new DatabaseHelper(this);
                db.addTransaction(
                        0.0,                 // valor provisório
                        "expense",            // tipo
                        "Nota Fiscal",        // banco
                        "Alimentação"         // categoria
                );

                // 🔹 ABRE SITE DA SEFAZ
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(qrContent));
                startActivity(browser);

                Toast.makeText(this,
                        "Nota fiscal registrada. Edite o valor depois.",
                        Toast.LENGTH_LONG).show();
            }
        }

        finish();
    }
}
