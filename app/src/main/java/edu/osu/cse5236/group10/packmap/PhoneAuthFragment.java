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

import java.util.concurrent.TimeUnit;


public class PhoneAuthFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "PhoneAuthFragment";

    private EditText mAuthCode;
    private EditText mPhoneNum;

    private Button mSubmitButton;
    private Button mBackButton;
    private Button mSendCodeButton;

    //private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Activity mPhoneAuth;
    private String mVerificationId;
    //private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoneAuth = getActivity();
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.send_code_button:
                String pNum = mPhoneNum.getText().toString();
                if (pNum.equals("")) {
                    Toast.makeText(this.getContext(), "You need to enter the phone number", Toast.LENGTH_SHORT).show();
                } else {
                    //startPhoneVerification(pNum);
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
