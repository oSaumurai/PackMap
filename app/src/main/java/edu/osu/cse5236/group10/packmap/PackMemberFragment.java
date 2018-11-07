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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PackMemberFragment extends Fragment {
    private static final String TAG = "PackMemberFragment";

    private RecyclerView mRecyclerView;
    private ArrayList<String> mUsers;
    private MemberListAdapter memberAdapter;
    private FirebaseFirestore db;

    private String mUserId;
    private String mGroupId;

    public PackMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.member_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.add_member:
                Log.d(TAG, "Add member");
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment = new InviteMemberFragment();
                Bundle bundle = new Bundle();
                bundle.putString("groupId", mGroupId);
                bundle.putStringArrayList("users", mUsers);
                fragment.setArguments(bundle);
                transaction.replace(R.id.pack_fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
        }

        return false;
    }

    // TODO: Rename and change types and number of parameters
    public static PackFragment newInstance() {
        PackFragment fragment = new PackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString("userId");
            mGroupId = getArguments().getString("groupId");
            Log.d(TAG, "" + mUserId + " - " + mGroupId);
        }

        setHasOptionsMenu(true);
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

        return v;
    }
}
