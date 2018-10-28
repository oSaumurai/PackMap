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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.User;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountSettingFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "AccountSettingFragment";
    private static final String EXTRA_PHONE = "userId";

    private EditText mFirstName;
    private EditText mLastName;
    private Button mSubmitButton;
    private Button mBackButton;

    private Activity settingActivity;
    private String mFirstnameCheck;
    private String mLastnameCheck;
    private String mPhoneNum;

    private UserStore userStore;

    public AccountSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoneNum = getArguments().getString(EXTRA_PHONE);

        Log.d(TAG, mPhoneNum);

        userStore = UserStore.getInstance();

        settingActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_setting, container, false);

        mFirstName = v.findViewById(R.id.setting_first_name);
        mLastName = v.findViewById(R.id.setting_last_name);
        mSubmitButton = v.findViewById(R.id.submit_setting);
        mBackButton = v.findViewById(R.id.account_setting_back);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

        // Get the fname and lname of the user
        userStore.processUser(mPhoneNum, task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    mFirstnameCheck = data.get("firstName").toString();
                    mLastnameCheck = data.get("lastName").toString();
                    mFirstName.setText(mFirstnameCheck);
                    mLastName.setText(mLastnameCheck);
                    Log.d(TAG, "Success replacing names");
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        return v;
    }

    private String getText(TextView tv) {
        return tv.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_setting:
                String tempFirstName = getText(mFirstName), tempLastName = getText(mLastName);

                if (!(tempFirstName.equals(mFirstnameCheck) &&
                        tempLastName.equals(mLastnameCheck))) {
                    userStore.updateUserName(mPhoneNum, tempFirstName, tempLastName);
                }

                settingActivity.finish();
                break;

            case R.id.account_setting_back:
                settingActivity.finish();
                break;
        }
    }
}
