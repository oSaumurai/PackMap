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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.osu.cse5236.group10.packmap.data.model.User;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "SignUpFragment";

    private EditText mPhoneNum;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private List<EditText> fieldList;

    private Button mSubmitButton;

    private Activity mSignUp;
    private UserStore userStore;

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
        mConfirmPassword = v.findViewById(R.id.sign_up_reenter_password);
        mSubmitButton = v.findViewById(R.id.submit_sign_up);

        fieldList = new ArrayList<>();
        fieldList.add(mPhoneNum);
        fieldList.add(mFirstName);
        fieldList.add(mLastName);
        fieldList.add(mPassword);
        fieldList.add(mConfirmPassword);

        mSubmitButton.setOnClickListener(this);
        mSignUp = getActivity();
        userStore = UserStore.getInstance();

        return v;
    }

    private String getText(TextView tv) {
        return tv.getText().toString().trim();
    }

    private void createAccount(String phoneNum, String lname, String fname, String password) {
        try {
            User u = new User(phoneNum, lname, fname, AESCrypt.encrypt(password));
            userStore.checkThenSetNewUser(u, new AddNewUserOnCompleteListener(u));
        } catch (Exception e) {
            Log.d(TAG, "Unable to encrypt password");

        }
    }

    public class AddNewUserOnCompleteListener implements OnCompleteListener<DocumentSnapshot> {
        private User newUser;

        private AddNewUserOnCompleteListener(User user) {
            this.newUser = user;
        }

        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "User already exists");
                    mPhoneNum.setError(getString(R.string.err_msg_user_exist));
                } else {
                    userStore.setNewUser(
                            newUser,
                            aVoid -> {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                mSignUp.finish();
                            },
                            e -> Log.w(TAG, "Error writing document", e));
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        }
    }

    private boolean haveEmptyField() {
        boolean check = false;

        for (EditText e : fieldList) {
            if (e.getText().length() == 0) {
                e.setError(getString(R.string.err_msg_empty));
                check = true;
            }
        }

        if (!mPassword.getText().equals(mConfirmPassword.getText())) {
            mConfirmPassword.setError(getString(R.string.err_msg_password));
            check = true;
        }

        return check;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_sign_up:
                if (!haveEmptyField())
                    createAccount(getText(mPhoneNum), getText(mLastName), getText(mFirstName), getText(mPassword));
                break;
        }
    }
}
