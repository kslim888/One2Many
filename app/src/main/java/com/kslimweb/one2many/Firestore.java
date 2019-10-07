package com.kslimweb.one2many;

import android.util.Log;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firestore {

    private static final String TAG = Firestore.class.getSimpleName();

    public void postToFirestore(FirebaseAuth mAuth, Spinner roleSpinner, FirebaseFirestore db) {
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
    }
}
