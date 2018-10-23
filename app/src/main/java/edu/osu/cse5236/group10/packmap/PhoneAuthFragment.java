package edu.osu.cse5236.group10.packmap;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class PhoneAuthFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "PhoneAuthFragment";

    private EditText mAuthCode;
    private EditText mPhoneNum;

    private Button mSubmitButton;
    private Button mBackButton;
    private Button mSendCodeButton;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Activity mPhoneAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoneAuth = getActivity();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_auth, container, false);

        mPhoneNum = v.findViewById(R.id.phone_auth_num);
        mAuthCode = v.findViewById(R.id.phone_auth_code);
        mSubmitButton = v.findViewById(R.id.phone_auth_submit);
        mBackButton = v.findViewById(R.id.phone_auth_back);
        mSendCodeButton = v.findViewById(R.id.send_code_button);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
        mSendCodeButton.setOnClickListener(this);

        return v;
    }

    private void startPhoneVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                mPhoneAuth,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.send_code_button:
                String pNum = mPhoneNum.getText().toString();
                if (pNum.equals("")) {
                    Toast.makeText(this.getContext(), "You need to enter the phone number", Toast.LENGTH_SHORT).show();
                } else {
                    startPhoneVerification(pNum);
                }
                break;

            case R.id.phone_auth_back:
                if (mPhoneAuth != null)
                    mPhoneAuth.finish();
                break;

            case R.id.phone_auth_submit:

                break;
        }
    }
}
