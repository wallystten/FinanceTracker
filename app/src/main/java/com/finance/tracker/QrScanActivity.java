package com.finance.tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QrScanActivity extends Activity {

    private static final int CAMERA_REQUEST = 100;
    private DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeView = new DecoratedBarcodeView(this);
        setContentView(barcodeView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST
            );
        } else {
            iniciarCamera();
        }
    }

    private void iniciarCamera() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result == null) return;

                barcodeView.pause();

                String qrContent = result.getText();

                if (isNotaFiscal(qrContent)) {
                    Toast.makeText(
                            QrScanActivity.this,
                            "🧾 Nota fiscal detectada\nFunção Premium",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            QrScanActivity.this,
                            "QR comum:\n" + qrContent,
                            Toast.LENGTH_LONG
                    ).show();
                }

                finish();
            }
        });
    }

    // 🔍 Detecta QR de nota fiscal brasileira
    private boolean isNotaFiscal(String content) {
        if (content == null) return false;

        try {
            Uri uri = Uri.parse(content);
            String host = uri.getHost();
            if (host == null) return false;

            host = host.toLowerCase();

            return host.contains("sefaz")
                    || host.contains("nfce")
                    || host.contains("nfe")
                    || host.contains("fazenda");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            iniciarCamera();
        } else {
            Toast.makeText(this,
                    "Permissão de câmera negada",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}
