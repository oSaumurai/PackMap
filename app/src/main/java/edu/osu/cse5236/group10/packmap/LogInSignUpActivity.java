package edu.osu.cse5236.group10.packmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class LogInSignUpActivity extends AppCompatActivity {

    private static final String TAG = "LogInSignUpActivity";

    // Buttons
    private Button mLoginButton;
    private Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG + ": onCreate() Called");

        setContentView(R.layout.activity_log_in_sign_up);

        // Find buttons by ID
        mLoginButton = findViewById(R.id.btn_log_in);
        mSignupButton = findViewById(R.id.btn_sign_up);

        // Register Listener for widgets
        mLoginButton.setOnClickListener(e -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}