package edu.osu.cse5236.group10.packmap.data.store;

import com.annimon.stream.Stream;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
    public void addActivity(String groupUid, String activityUid){
        DocumentReference dr = db.collection("groups").document(groupUid);
        dr.update("activityList", FieldValue.arrayUnion(activityUid));
    }

    public void addGroup(Group group) {
        addDocument(group, task -> {
            Stream.of(group.getUserList())
                    .forEach(userId -> UserStore.getInstance().addGroup(userId, task.getId()));
        }, getOnFailureListener());
    }

    public void addUserToGroup(String group, String userId) {
        DocumentReference dr = db.collection(COLLECTION).document(group);

        dr.update("userList", FieldValue.arrayUnion(userId));
    }

    public void getGroupsByUserId(String userPhoneNumber,
                                  OnCompleteListener<QuerySnapshot> listener) {
        db.collection(COLLECTION)
                .whereArrayContains("userList", userPhoneNumber)
                .get()
                .addOnCompleteListener(listener);
    }

    public void getGroup(String groupId,
                         OnCompleteListener<DocumentSnapshot> listener) {
        getDocument(groupId, listener, getOnFailureListener());
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
