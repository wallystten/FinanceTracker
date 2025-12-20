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

        // Inicia leitor de QR Code
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Aponte para o QR Code da Nota Fiscal");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                String url = result.getContents();

                try {
                    // Abre o site da SEFAZ
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);

                } catch (Exception e) {
                    Toast.makeText(this, "Erro ao abrir a nota fiscal", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
            }

            // 🔴 ESSA LINHA É A CHAVE DO PROBLEMA
            finish();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}    
