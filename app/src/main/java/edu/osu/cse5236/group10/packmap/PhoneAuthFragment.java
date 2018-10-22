package edu.osu.cse5236.group10.packmap;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class PhoneAuthFragment extends Fragment implements View.OnClickListener {

    private EditText mAuthCode;

    private Button mSubmitButton;
    private Button mBackButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_auth, container, false);

        mAuthCode = v.findViewById(R.id.phone_auth_code);
        mSubmitButton = v.findViewById(R.id.phone_auth_submit);
        mBackButton = v.findViewById(R.id.phone_auth_back);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.phone_auth_submit:

                break;

            case R.id.phone_auth_back:
                Activity phoneAuth = getActivity();
                if (phoneAuth != null)
                    phoneAuth.finish();
                break;
        }
    }
}
