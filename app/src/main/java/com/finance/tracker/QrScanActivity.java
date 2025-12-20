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

            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri
