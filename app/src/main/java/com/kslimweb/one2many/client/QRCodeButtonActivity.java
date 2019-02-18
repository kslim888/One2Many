package com.kslimweb.one2many.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.kslimweb.one2many.LoginActivity;
import com.kslimweb.one2many.R;
import com.kslimweb.one2many.host.SetHostActivity;

import java.util.Set;

public class QRCodeButtonActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = QRCodeButtonActivity.class.getSimpleName();
    private static final int MY_PERMISSION_REQUEST_CAMERA = 1;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }

        Button button = findViewById(R.id.read_qr_button);
        button.setOnClickListener(v -> {
            Log.d(TAG, "onClick: ");
            startActivity(new Intent(QRCodeButtonActivity.this, ScanQRCode.class));
        });
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(findViewById(android.R.id.content), "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", view ->
                    ActivityCompat.requestPermissions(QRCodeButtonActivity.this, new String[] {
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_REQUEST_CAMERA)).show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: Sign out");

        new MaterialDialog.Builder(QRCodeButtonActivity.this)
                .icon(getResources().getDrawable(R.drawable.ic_warning_black_24dp))
                .title("Logging Out")
                .content("Are you sure you log out ?")
                .positiveText("Yes")
                .negativeText("No")
                .onAny((dialog, which) -> {
                    if(which == DialogAction.POSITIVE) {
                        finish();
                        // firebase sign out
                        mAuth.signOut();

                        // google sign out
                        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                                task -> {
                                    startActivity(new Intent(QRCodeButtonActivity.this, LoginActivity.class));
                                });
                    }
                })
                .show();
        return super.onOptionsItemSelected(item);
    }
}
