package com.finance.tracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QrScanActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private DecoratedBarcodeView barcodeView;
    private boolean jaLeu = false; // trava leitura dupla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        barcodeView = findViewById(R.id.barcode_scanner);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST
            );
        } else {
            iniciarScanner();
        }
    }

    private void iniciarScanner() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {

                if (result == null || result.getText() == null) return;
                if (jaLeu) return; // evita loop

                jaLeu = true;
                barcodeView.pause();

                String url = result.getText();

                if (url.startsWith("http")) {
                    Intent intent = new Intent(QrScanActivity.this, SefazWebActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                } else {
                    Toast.makeText(QrScanActivity.this,
                            "QR Code inválido",
                            Toast.LENGTH_LONG).show();
                }

                finish(); // encerra a câmera corretamente
            }
        });

        barcodeView.resume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeView != null && !jaLeu) {
            barcodeView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeView != null) {
            barcodeView.pause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarScanner();
            } else {
                Toast.makeText(this,
                        "Permissão da câmera negada",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
