package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.IntentIntegrator;
import com.journeyapps.barcodescanner.IntentResult;

public class QrScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        IntentResult result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {

                String urlNota = result.getContents();

                // Volta para o app com o LINK
                Intent back = new Intent();
                back.putExtra("note_url", urlNota);
                setResult(RESULT_OK, back);
                finish();

                // Abre a SEFAZ
                try {
                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(urlNota));
                    startActivity(browser);
                } catch (Exception e) {
                    Toast.makeText(this, "Não foi possível abrir o site da nota", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
