package com.kslimweb.one2many.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = ScanQRCodeActivity.class.getSimpleName();
    ZXingScannerView scannerView;

    // Class-Topic will become [test-test] for development
    public static String SUBSCRIBE_TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {
        Log.d(TAG, "handleResult: " + result.getText());
        String qrCodeValue = result.getText();
        if (qrCodeValue.contains("One2Many")) {
            SUBSCRIBE_TOPIC = qrCodeValue;
            startActivity(new Intent(ScanQRCodeActivity.this, ClientTranslationActivity.class));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid QR Code Scanned")
                    .setMessage("Please make sure you scan the right QR code displayed by One2Many app")
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            scannerView.resumeCameraPreview(this))
                    .show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
