package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class QrScanActivity extends Activity {

    private static final int QR_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        abrirScanner();
    }

    private void abrirScanner() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, QR_REQUEST_CODE);
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

        if (requestCode == QR_REQUEST_CODE) {

            if (resultCode == RESULT_OK && data != null) {

                String qrContent = data.getStringExtra("SCAN_RESULT");

                if (qrContent != null && qrContent.startsWith("http")) {
                    Intent browserIntent =
                            new Intent(Intent.ACTION_VIEW, Uri.parse(qrContent));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(
                            this,
                            "QR Code lido, mas não é um link válido.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            finish();
        }
    }
}
