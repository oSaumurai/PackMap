package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import edu.osu.cse5236.group10.packmap.data.store.GroupStore;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;


public class InviteMemberFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "InviteMemberFragment";

    private String mGroupId;
    private ArrayList<String> userList;

    private EditText mMemberId;
    private Button mSubmitButton;
    private Button mBackButton;

    public InviteMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroupId = getArguments().getString("groupId");
            userList = getArguments().getStringArrayList("users");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invite_member, container, false);

        mMemberId = v.findViewById(R.id.member_id);
        mSubmitButton = v.findViewById(R.id.submit_invite_member);
        mBackButton = v.findViewById(R.id.back);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

        return v;
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
                }
                break;

            case R.id.back:

                break;
        }
    }
}