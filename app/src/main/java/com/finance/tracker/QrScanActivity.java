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

        // Usa leitor externo padrão (câmera do sistema)
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

        try {
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Leitor de QR Code não encontrado",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String qrContent = data.getStringExtra("SCAN_RESULT");

            if (qrContent == null) {
                finish();
                return;
            }

            // ✅ VALIDA SEFAZ SC
            if (!qrContent.contains("sef.sc.gov.br")) {
                Toast.makeText(this,
                        "Nota não pertence à SEFAZ SC",
                        Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // ✅ SALVA NO BANCO COMO PENDENTE
            DatabaseHelper db = new DatabaseHelper(this);
            db.addTransaction(
                    0.0,
                    "expense",
                    "SEFAZ SC",
                    "Nota fiscal",
                    "pendente"
            );

            Toast.makeText(this,
                    "Nota fiscal registrada (pendente)",
                    Toast.LENGTH_LONG).show();

            // ✅ ABRE A SEFAZ
            Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(qrContent));
            startActivity(browserIntent);

            finish();
        } else {
            finish();
        }
    }
}
