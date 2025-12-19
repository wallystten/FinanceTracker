package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class QrScanActivity extends Activity {

    private static final int ZXING_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        abrirLeitorZXing();
    }

    private void abrirLeitorZXing() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, ZXING_REQUEST);
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Leitor de QR Code não encontrado.\nInstale o app ZXing.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ZXING_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                String qrResult = data.getStringExtra("SCAN_RESULT");

                if (qrResult != null && qrResult.startsWith("http")) {
                    abrirNoNavegador(qrResult);
                } else {
                    Toast.makeText(this, "QR Code inválido", Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }
    }

    private void abrirNoNavegador(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
