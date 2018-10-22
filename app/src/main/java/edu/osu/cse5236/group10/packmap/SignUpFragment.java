package edu.osu.cse5236.group10.packmap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    private EditText mPhoneNum;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPassword;

    private Button mSubmitButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mPhoneNum = v.findViewById(R.id.sign_up_phone_number);
        mFirstName = v.findViewById(R.id.sign_up_first_name);
        mLastName = v.findViewById(R.id.sign_up_last_name);
        mPassword = v.findViewById(R.id.sign_up_password);
        mSubmitButton = v.findViewById(R.id.submit_sign_up);

        mSubmitButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_sign_up:

                break;
        }
    }
}
