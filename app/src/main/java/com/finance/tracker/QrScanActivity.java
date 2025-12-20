package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicia o leitor ZXing interno
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Aponte a câmera para o QR Code da nota fiscal");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                String qrContent = result.getContents();

                // 1️⃣ Abre o site da SEFAZ
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(qrContent));
                startActivity(browserIntent);

                // 2️⃣ Abre a tela de confirmação no app
                Intent i = new Intent(this, AddExpenseActivity.class);
                i.putExtra("type", "expense");
                i.putExtra("category", "Nota Fiscal");
                i.putExtra("bank", "NFC-e");
                startActivity(i);

                // 3️⃣ Fecha o QR Scan
                finish();
            } else {
                Toast.makeText(this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
