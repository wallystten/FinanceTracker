package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrScanActivity extends Activity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Escaneie o QR Code da Nota Fiscal");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {

            String linkNota = result.getContents();
            String estado = "SC"; // base atual

            double valor = 0.0; // pode evoluir depois

            db.addTransaction(
                    valor,
                    "expense",
                    "Nota Fiscal - " + estado,
                    "Nota Fiscal",
                    linkNota
            );

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(linkNota));
            startActivity(i);
            finish();

        } else {
            Toast.makeText(this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
