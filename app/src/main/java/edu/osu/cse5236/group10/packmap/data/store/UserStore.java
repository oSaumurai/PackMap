package edu.osu.cse5236.group10.packmap.data.store;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.SignUpFragment;
import edu.osu.cse5236.group10.packmap.data.model.User;

public class UserStore extends AbstractStore {

    private static final String TAG = "UserStore";
    private static final String COLLECTION = "users";

    private UserStore() {
        super();
    }

    private static UserStore instance;

    public static UserStore getInstance() {
        if (instance == null) {
            instance = new UserStore();
        }
        return instance;
    }

    public void checkThenSetNewUser(User user,
                                    SignUpFragment.AddNewUserOnCompleteListener onCompleteListener) {
        getDocument(user, onCompleteListener, getOnFailureListener());
    }

    public void setNewUser(User user, OnSuccessListener<Void> onSuccessListener,
                           OnFailureListener onFailureListener) {
        setDocument(user, onSuccessListener, onFailureListener);
    }

    public void processUser(String uid, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        getDocument(uid, onCompleteListener, getOnFailureListener());
    }

    public void updateUserName(String uid, String newFirstName, String newLastName) {
        DocumentReference dr = db.collection(COLLECTION).document(uid);
        dr.update("firstName", newFirstName);
        dr.update("lastName", newLastName);
    }

    public void addGroup(String uid, String groupId) {
        DocumentReference dr = db.collection("users").document(uid);

        dr.update("groups", FieldValue.arrayUnion(groupId));
    }

    public void deleteGroup(String uid, String groupId) {
        DocumentReference dr = db.collection("users").document(uid);

        dr.update("groups", FieldValue.arrayRemove(groupId));
    }

    public void deleteUserById(String userId) {
        deleteDocumentById(userId);
    }

    public void setUser(User user) {
        setDocument(user);
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
