package edu.osu.cse5236.group10.packmap;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.osu.cse5236.group10.packmap.data.model.User;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "SignUpFragment";

    private EditText mPhoneNum;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPassword;

    private Button mSubmitButton;

    private Activity mSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mPhoneNum = v.findViewById(R.id.sign_up_phone_num);
        mFirstName = v.findViewById(R.id.sign_up_first_name);
        mLastName = v.findViewById(R.id.sign_up_last_name);
        mPassword = v.findViewById(R.id.sign_up_password);
        mSubmitButton = v.findViewById(R.id.submit_sign_up);

        mSubmitButton.setOnClickListener(this);
        mSignUp = getActivity();

        return v;
    }

    private String getText(TextView tv) {
        return tv.getText().toString().trim();
    }

    private void createAccount(String phoneNum, String lname, String fname, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User u = new User(phoneNum, lname, fname, password);
        db.collection("users").document(u.getPhone())
                .set(u)
                .addOnSuccessListener(aVoid ->
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                )
                .addOnFailureListener(e ->
                        Log.w(TAG, "Error writing document", e)
                );
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_sign_up:
                createAccount(getText(mPhoneNum), getText(mLastName), getText(mFirstName), getText(mPassword));
                mSignUp.finish();
                break;
        }
    }
}
