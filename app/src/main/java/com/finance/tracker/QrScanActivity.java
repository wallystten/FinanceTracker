package com.finance.tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QrScanActivity extends Activity {

    private static final int CAMERA_REQUEST = 100;
    private DecoratedBarcodeView barcodeView;
    private boolean qrLido = false;

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
                if (result == null || qrLido) return;

                qrLido = true;
                barcodeView.pause();

                String qrContent = result.getText();

                if (isNotaFiscal(qrContent)) {
                    mostrarTelaNota(qrContent);
                } else {
                    Toast.makeText(
                            QrScanActivity.this,
                            "QR lido:\n" + qrContent,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    private void mostrarTelaNota(String urlOriginal) {

        // 🔧 CORREÇÃO CRÍTICA
        String url = urlOriginal.trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);
        layout.setGravity(Gravity.CENTER);

        Button btnAbrir = new Button(this);
        btnAbrir.setText("🧾 Abrir nota fiscal (SEFAZ)");
        btnAbrir.setOnClickListener(v -> {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Não foi possível abrir o navegador",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        Button btnVoltar = new Button(this);
        btnVoltar.setText("⬅ Voltar");
        btnVoltar.setOnClickListener(v -> finish());

        layout.addView(btnAbrir);
        layout.addView(btnVoltar);

        setContentView(layout);

        Toast.makeText(
                this,
                "Nota fiscal detectada\nImportação automática será Premium",
                Toast.LENGTH_LONG
        ).show();
    }

    private boolean isNotaFiscal(String content) {
        if (content == null) return false;

        try {
            Uri uri = Uri.parse(content.startsWith("http") ? content : "https://" + content);
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
        if (barcodeView != null) barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeView != null) barcodeView.pause();
    }
}
