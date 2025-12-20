package com.finance.tracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class QrScanActivity extends Activity {

    private static final int QR_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

        try {
            startActivityForResult(intent, QR_REQUEST);
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Leitor de QR Code não encontrado. Instale o ZXing.",
                    Toast.LENGTH_LONG
            ).show();

            Intent playStoreIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android")
            );
            startActivity(playStoreIntent);

            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_REQUEST && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("SCAN_RESULT");

            if (result != null && result.startsWith("http")) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result)));
            }
        }

        finish();
    }
}
