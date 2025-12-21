package com.finance.tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

            if (result.getContents() == null) {
                finish();
                return;
            }

            String qrUrl = result.getContents();
            String estado = identificarEstado(qrUrl);

            Toast.makeText(
                    this,
                    "Nota fiscal detectada: " + estado,
                    Toast.LENGTH_LONG
            ).show();

            // Abre site da SEFAZ
            Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(qrUrl));
            startActivity(browserIntent);

            // Pergunta o valor da nota
            pedirValorNota(estado, qrUrl);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void pedirValorNota(String estado, String linkNota) {

        EditText input = new EditText(this);
        input.setHint("Ex: 123,45");

        new AlertDialog.Builder(this)
                .setTitle("Registrar gasto")
                .setMessage("Informe o valor da nota fiscal\n(" + estado + ")")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("Salvar", (dialog, which) -> {

                    String valorTexto = input.getText().toString()
                            .replace(",", ".");

                    try {
                        double valor = Double.parseDouble(valorTexto);

                        DatabaseHelper db = new DatabaseHelper(this);
                        db.addTransaction(
                                valor,
                                "expense",
                                "Nota Fiscal - " + estado,
                                "Nota Fiscal"
                        );

                        Toast.makeText(
                                this,
                                "Gasto registrado com sucesso",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();

                    } catch (Exception e) {
                        Toast.makeText(
                                this,
                                "Valor inválido",
                                Toast.LENGTH_LONG
                        ).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> finish())
                .show();
    }

    private String identificarEstado(String url) {

        if (url.contains("sef.sc.gov.br")) return "Santa Catarina";
        if (url.contains("fazenda.sp.gov.br")) return "São Paulo";
        if (url.contains("sefaz.pr.gov.br")) return "Paraná";
        if (url.contains("sefaz.rs.gov.br")) return "Rio Grande do Sul";

        return "Estado não identificado";
    }
}
