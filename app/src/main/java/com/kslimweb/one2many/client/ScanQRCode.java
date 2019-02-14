package com.kslimweb.one2many.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;
import com.kslimweb.one2many.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = ScanQRCode.class.getSimpleName();
    ZXingScannerView scannerView;
    public static String SUBSCRIBE_TOPIC = "";

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
        SUBSCRIBE_TOPIC = qrCodeValue;
        startActivity(new Intent(ScanQRCode.this, ClientTranslation.class));
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
