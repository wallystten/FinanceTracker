package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class QrScanActivity extends Activity {

    private static final int REQUEST_QR_SCAN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Intent padrão para leitura de QR Code
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, REQUEST_QR_SCAN);

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Nenhum leitor de QR Code encontrado no dispositivo",
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_QR_SCAN && resultCode == RESULT_OK && data != null) {
            String qrContent = data.getStringExtra("SCAN_RESULT");

            if (qrContent != null && qrContent.startsWith("http")) {
                // Abre o site da SEFAZ no navegador
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(qrContent));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "QR Code inválido", Toast.LENGTH_SHORT).show();
            }
        }

        // Sempre volta para o app
        finish();
    }
}
