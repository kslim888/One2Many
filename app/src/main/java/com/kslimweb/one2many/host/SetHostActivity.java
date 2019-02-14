package com.kslimweb.one2many.host;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.kslimweb.one2many.LoginActivity;
import com.kslimweb.one2many.R;

import java.util.Set;

public class SetHostActivity extends AppCompatActivity {

    private static final String TAG = SetHostActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    EditText className;
    EditText topicName;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_host);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button button = findViewById(R.id.generate_qr_code);
        className = findViewById(R.id.class_name);
        topicName = findViewById(R.id.topic_name);

        button.setOnClickListener(v -> {

            if(validate()) {
                String classNameString = className.getText().toString();
                String topicNameString = topicName.getText().toString();

                startActivity(new Intent(SetHostActivity.this, ShowQRCode.class)
                        .putExtra("CLASS_NAME", classNameString)
                        .putExtra("TOPIC_NAME", topicNameString)
                );
            }

//            mAuth.signOut();
//            Toast.makeText(SetHostActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(SetHostActivity.this, LoginActivity.class));

        });
    }

    public boolean validate() {
        boolean valid = true;

        String classNameString = className.getText().toString();
        String topicNameString = topicName.getText().toString();

        if (classNameString.isEmpty()) {
            className.setError("Class name is empty");
            valid = false;
        } else {
            className.setError(null);
        }

        if (topicNameString.isEmpty()) {
            topicName.setError("Topic name is empty");
            valid = false;
        } else {
            topicName.setError(null);
        }

        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: Sign out");

        new MaterialDialog.Builder(SetHostActivity.this)
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
                                    startActivity(new Intent(SetHostActivity.this, LoginActivity.class));
                                });
                    }
                })
                .show();

        return super.onOptionsItemSelected(item);
    }
}
