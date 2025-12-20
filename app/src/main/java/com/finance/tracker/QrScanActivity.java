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

        // Usa leitor nativo via intent (sem ZXing externo obrigatório)
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 1001);
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Leitor de QR não disponível. Use a câmera do celular.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            String qrContent = data.getStringExtra("SCAN_RESULT");

            if (qrContent != null && qrContent.startsWith("http")) {
                // Abre site da SEFAZ
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(qrContent));
                startActivity(browser);

                // Depois abre tela de adicionar gasto manualmente
                Intent i = new Intent(this, NfceAddActivity.class);
                i.putExtra("url", qrContent);
                startActivity(i);
            }

            finish();
        } else {
            finish();
        }
    }
}
