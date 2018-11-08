package edu.osu.cse5236.group10.packmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.osu.cse5236.group10.packmap.data.store.ActivityStore;
import edu.osu.cse5236.group10.packmap.data.store.GroupStore;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;

public class CreateActivityActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateActivityActivity";

    private String mGroupId;
    private TextView mActivityName;
    private TextView mActivityInfo;
    private Button mSubmitButton;
    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);

        mGroupId = getIntent().getStringExtra("groupId");
        Log.d(TAG, "onCreate: GroupId:" + mGroupId);
        mActivityName = findViewById(R.id.activity_name);
        mActivityInfo=findViewById(R.id.activity_info);
        mSubmitButton = findViewById(R.id.submit_activity);
        mBackButton = findViewById(R.id.back);

        mSubmitButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_activity:
                if (mActivityName.getText().length() == 0)
                    mActivityName.setError(getString(R.string.err_msg_empty));
                else {
                    ActivityStore.getInstance().addNewActivity(mActivityName.getText().toString().trim(),mActivityInfo.getText().toString().trim(), mGroupId);
                    finish();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
