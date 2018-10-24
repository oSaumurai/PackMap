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

import edu.osu.cse5236.group10.packmap.data.model.User;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "SignUpFragment";

    private EditText mPhoneNum;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPassword;

    private Button mSubmitButton;

    private Activity mSignUp;
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();

        return v;
    }

    private String getText(TextView tv) {
        return tv.getText().toString().trim();
    }

    private void createAccount(String phoneNum, String lname, String fname, String password) {
        User u = new User(phoneNum, lname, fname, password);
        DocumentReference userRef = db.collection("users").document(u.getPhone());

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "User already exists");
                        Toast.makeText(mSignUp, "User already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        userRef.set(u)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    mSignUp.finish();
                                })
                                .addOnFailureListener(e ->
                                        Log.w(TAG, "Error writing document", e)
                                );
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_sign_up:
                createAccount(getText(mPhoneNum), getText(mLastName), getText(mFirstName), getText(mPassword));
                break;
        }
    }
}
