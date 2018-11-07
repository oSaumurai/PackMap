package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import edu.osu.cse5236.group10.packmap.data.store.GroupStore;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;


public class InviteMemberActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InviteMemberActivity";

    private String mGroupId;
    private ArrayList<String> userList;

    private EditText mMemberId;
    private Button mSubmitButton;
    private Button mBackButton;

    public InviteMemberActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);

        mGroupId = getIntent().getStringExtra("groupId");
        userList = getIntent().getStringArrayListExtra("users");

        mMemberId = findViewById(R.id.member_id);
        mSubmitButton = findViewById(R.id.submit_invite_member);
        mBackButton = findViewById(R.id.invite_member_back);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_invite_member:
                String userId = mMemberId.getText().toString().trim();

                if (userList.contains(userId)) {
                    mMemberId.setError("User already in the group");
                } else {
                    Log.d(TAG, "ff: " + mGroupId + " - " + userId);
                    GroupStore.getInstance().addUserToGroup(mGroupId, userId);
                    UserStore.getInstance().addGroup(userId, mGroupId);
                    finish();
                }
                break;

            case R.id.back:
                finish();
                break;
        }
    }
}