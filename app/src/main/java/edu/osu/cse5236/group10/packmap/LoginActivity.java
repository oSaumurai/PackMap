package edu.osu.cse5236.group10.packmap;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mPhoneView;
    private EditText mPasswordView;
    private FirebaseFirestore db;
    private Activity loginActivity;

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, TAG + ": onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, TAG + ": onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, TAG + ": onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG + ": onDestroy() called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, TAG + ": onCreate() Called");

        loginActivity = this;

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mPhoneView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        Button mPhoneSignInButton = findViewById(R.id.phone_sign_in_button);

        db = FirebaseFirestore.getInstance();

        mPhoneSignInButton.setOnClickListener(this);
    }

    private boolean haveEmptyField() {
        boolean check = false;

        if (mPhoneView.getText().length() == 0) {
            mPhoneView.setError(getString(R.string.err_msg_empty));
            check = true;
        }
        if (mPasswordView.getText().length() == 0) {
            mPasswordView.setError(getString(R.string.err_msg_empty));
            check = true;
        }

        return check;
    }

    private void loginUser(String phone, String password) {
        DocumentReference userRef = db.collection("users").document(phone);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (document.getString("password").equals(password)) {
                        Log.d(TAG, TAG + ": User Log in!");
                        startActivity(new Intent(this, PackListActivity.class));
                    } else {
                        Log.d(TAG, "Wrong password");
                        Toast.makeText(loginActivity, R.string.err_msg_user_login_failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "User not exists");
                    Toast.makeText(loginActivity, R.string.err_msg_user_login_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.phone_sign_in_button:
                if (!haveEmptyField())
                    loginUser(mPhoneView.getText().toString().trim(),
                            mPasswordView.getText().toString().trim());
                break;
        }
    }
}

