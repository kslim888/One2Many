package com.kslimweb.one2many;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private final String TAG = SignUp.class.getSimpleName();
    public static boolean fromEmailSignUp = false;
    private FirebaseAuth mAuth;
    private EditText password, email;
    private TextView signUp;
    Spinner roleSpinner;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sign_up);

        findViews();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signUp.setOnClickListener(v -> signUpAccount());
    }

    private void findViews() {
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        roleSpinner = findViewById(R.id.role_spinner);
        signUp = findViewById(R.id.signUp);
    }

    public void signUpAccount() {
        Log.i(TAG, "SignUp");

        if (!validate()) { return; }

        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        fromEmailSignUp = true;

        final String emailString = email.getText().toString().trim();
        final String passwordString = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        // Sign up success
                        Log.i(TAG, "createUserWithEmail:success");
                        postToFirestore();
                        progressDialog.dismiss();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.i(TAG, "createUserWithEmail:failure", task.getException());

                        new MaterialDialog.Builder(SignUp.this)
                                .title("Registration Failed")
                                .content(Objects.requireNonNull(task.getException()).getMessage())
                                .positiveText("Return")
                                .show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void sendVerificationEmail() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // After email is sent just logout the user and finish this activity
                    Log.i(TAG, "Send verification email: success", task.getException());

                    MaterialDialog materialDialog = new MaterialDialog.Builder(SignUp.this)
                            .title("Registration Success")
                            .content("Please verify the email before login")
                            .positiveText("Ok")
                            .build();

                    materialDialog.setOnDismissListener(dialog -> {
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(SignUp.this, LoginActivity.class));

                    });
                    materialDialog.show();

                } else {
                    // email not sent, so display message
                    Log.i(TAG, "Send verification email: fail", task.getException());
                    new MaterialDialog.Builder(SignUp.this)
                            .title("Send Verification Email Failed")
                            .content(Objects.requireNonNull(task.getException()).getMessage())
                            .positiveText("Return")
                            .show();
                }
            });
        }
    }

    public boolean validate() {
        boolean valid = true;

        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString();

        if (emailString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordString.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private void postToFirestore() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Log.d(TAG, "postToFirestore: firebaseUser is null");
            return;
        }
        Log.d(TAG, "postToFirestore: firebaseUser is not null");
        Log.d(TAG, "postToFirestore: " + firebaseUser.getEmail());
        Log.d(TAG, "postToFirestore: " + roleSpinner.getSelectedItem().toString());

        // Custom document ID = user UID
        //Reference to user collection but custom document ID
        DocumentReference userCollection =
                db.collection("users")
                        .document(firebaseUser.getUid());

        Users user = new Users(firebaseUser.getEmail(), roleSpinner.getSelectedItem().toString());

        // send to Firebase listener
        userCollection.set(user)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "postToFirestore: Add User Success")
                )
                .addOnFailureListener(e ->
                        Log.d(TAG, "postToFirestore: Add User Failed")
                );
        sendVerificationEmail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return super.onCreateOptionsMenu(menu); }
}