package edu.osu.cse5236.group10.packmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PackMemberFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PackMemberFragment";

    private RecyclerView mRecyclerView;
    private ArrayList<String> mUsers;
    private MemberListAdapter memberAdapter;
    private FirebaseFirestore db;
    private Button mAddGroupButton;

    private String mUserId;
    private String mGroupId;

    public PackMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString("userId");
            mGroupId = getArguments().getString("groupId");
            Log.d(TAG, "" + mUserId + " - " + mGroupId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pack_member, container, false);

        // RecyclerView configuration
        mUsers = new ArrayList<>();
        memberAdapter = new MemberListAdapter(mUsers);

        mRecyclerView = v.findViewById(R.id.pack_member_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(memberAdapter);

        db = FirebaseFirestore.getInstance();

        db.collection("groups").document(mGroupId).addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.d(TAG, "error: " + e.getMessage());
            } else {
                List<String> temp = (List<String>) querySnapshot.get("userList");
                mUsers.clear();
                mUsers.addAll(temp);

                memberAdapter.notifyDataSetChanged();

                Log.d(TAG, "" + mUsers.size());
            }
        });

        mAddGroupButton = v.findViewById(R.id.add_member);
        mAddGroupButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_member:
                Log.d(TAG, "Add member");
                Intent intent = new Intent(getActivity(), InviteMemberActivity.class);
                intent.putExtra("users", mUsers);
                intent.putExtra("groupId", mGroupId);
                startActivity(intent);
                break;
        }
    }
}
