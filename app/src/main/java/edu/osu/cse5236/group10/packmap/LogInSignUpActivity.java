package edu.osu.cse5236.group10.packmap;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogInSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LogInSignUpActivity";

    // Buttons
    private Button mLoginButton;
    private Button mSignupButton;
    //layout
    private LinearLayout l1;
    private LinearLayout l2;
    //Text
    private TextView textView;
    //animation
    private Animation uptodown;
    private Animation downtoup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG + ": onCreate() Called");

        setContentView(R.layout.activity_log_in_sign_up);
        //
        l1=findViewById(R.id.login_frame_1);
        l2=findViewById(R.id.login_frame_2);

        if (UserSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), PackListActivity.class);
            intent.putExtra("userId", UserSharedPreference.getUserId(getApplicationContext()));
            startActivity(intent);
        }

        //
        uptodown=AnimationUtils.loadAnimation(this,R.anim.up_to_down);
        downtoup=AnimationUtils.loadAnimation(this,R.anim.down_to_up);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
        // Find buttons by ID
        mLoginButton = findViewById(R.id.btn_log_in);
        mSignupButton = findViewById(R.id.btn_sign_up);

        // Register Listener for widgets
        mLoginButton.setOnClickListener(this);
        mSignupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_log_in:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_sign_up:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
}
