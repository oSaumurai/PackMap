package edu.osu.cse5236.group10.packmap.data.store;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.Group;

public class GroupStore extends AbstractStore {

    private static final String TAG = "GroupStore";
    private static final String COLLECTION = "groups";

    private GroupStore() {
        super();
    }

    private static GroupStore instance;

    public static GroupStore getInstance() {
        if (instance == null) {
            instance = new GroupStore();
        }
        return instance;
    }

    public void addGroup(String group) {
        Group dummy = new Group();
        dummy.setName(group);
        addDocument(dummy);
    }

    public void addGroup(String group, String userId) {
        Group dummy = new Group();
        dummy.setName(group);
        dummy.getUserList().add(userId);
        addDocument(dummy, task -> {
            UserStore.getInstance().addGroup(userId, task.getId());
        }, getOnFailureListener());
    }

    public void getGroupsByUserId(String userPhoneNumber,
                                  OnCompleteListener<QuerySnapshot> listener) {
        db.collection(COLLECTION)
                .whereArrayContains("userList", userPhoneNumber)
                .get()
                .addOnCompleteListener(listener);
    }

    @Override
    protected String getCollection() {
        return COLLECTION;
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
