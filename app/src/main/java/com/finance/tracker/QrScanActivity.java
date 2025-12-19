package com.finance.tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QrScanActivity extends Activity {

    private static final int CAMERA_PERMISSION_REQUEST = 1001;

    private SurfaceView cameraPreview;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        cameraPreview = findViewById(R.id.camera_preview);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> finish());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST
            );
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        BarcodeDetector detector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource.Builder(this, detector)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new android.view.SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(android.view.SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            QrScanActivity.this,
                            Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED) {

                        cameraSource.start(cameraPreview.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(android.view.SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(android.view.SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (detections.getDetectedItems().size() > 0) {
                    String qrValue = detections.getDetectedItems().valueAt(0).displayValue;

                    runOnUiThread(() -> {
                        cameraSource.stop();

                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(qrValue));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(
                                    QrScanActivity.this,
                                    "QR Code lido, mas não foi possível abrir o link",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        finish();
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startCamera();
        } else {
            Toast.makeText(this, "Permissão da câmera negada", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
