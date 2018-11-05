package edu.osu.cse5236.group10.packmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.osu.cse5236.group10.packmap.data.store.GroupStore;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
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
        mSubmitButton = findViewById(R.id.submit_group);
        mBackButton = findViewById(R.id.back);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_group:
                if (mGroupName.getText().length() == 0)
                    mGroupName.setError(getString(R.string.err_msg_empty));
                else {
                    GroupStore.getInstance().addGroup(mGroupName.getText().toString().trim(), mUserId);
                    finish();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
