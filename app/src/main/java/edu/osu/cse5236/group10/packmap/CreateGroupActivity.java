package edu.osu.cse5236.group10.packmap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.osu.cse5236.group10.packmap.data.AddUserListContent;
import edu.osu.cse5236.group10.packmap.data.model.Group;
import edu.osu.cse5236.group10.packmap.data.store.GroupStore;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener,
        AddToPackFragment.OnAddUserListFragmentInteractionListener {

    private static final String TAG = "CreateGroupActivity";

    private String mUserId;
    private TextView mGroupName;
    private Button mSubmitButton;
    private Button mBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mUserId = getIntent().getStringExtra("userId");

        mGroupName = findViewById(R.id.group_name);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.add_to_pack_layout);
        if (fragment == null) {
            fragment = new AddToPackFragment();
            fm.beginTransaction()
                    .add(R.id.add_user_list_container, fragment)
                    .commit();
        }
        mSubmitButton = findViewById(R.id.submit_group);
        mBackButton = findViewById(R.id.back);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_group:
                List<String> groupUsers = AddUserListContent.getSelectedsUserId();
                if (mGroupName.getText().length() == 0) {
                    mGroupName.setError(getString(R.string.err_msg_empty));
                //else if (groupUsers.isEmpty()) {
                //    Toast.makeText(this, R.string.need_more_users, Toast.LENGTH_LONG).show();
                } else {
                    Group newGroup = new Group();
                    newGroup.setName(mGroupName.getText().toString().trim());
                    groupUsers.add(mUserId);
                    newGroup.setUserList(groupUsers);
                    GroupStore.getInstance().addGroup(newGroup);
                    finish();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(AddUserListContent.AddUserItem item) {

    }
}
