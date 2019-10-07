package com.kslimweb.one2many.client;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = ScanQRCodeActivity.class.getSimpleName();
    ZXingScannerView scannerView;

    // TODO set build variant to debug
    //  Class-Topic [test-test]
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
        SUBSCRIBE_TOPIC = qrCodeValue;
        startActivity(new Intent(ScanQRCodeActivity.this, ClientTranslationActivity.class));
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
