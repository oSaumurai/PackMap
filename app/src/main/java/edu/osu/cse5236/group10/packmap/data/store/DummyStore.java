package edu.osu.cse5236.group10.packmap.data.store;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.Group;

public class DummyStore extends AbstractStore {

    private static final String TAG = "DummyStore";
    private static final String COLLECTION = "dummy";

    public void addDummy(String group) {
        Group dummy = new Group();
        dummy.setName(group);
        addDocument(dummy);
    }

    public void updateDummies(OnCompleteListener<QuerySnapshot> listener) {
        getAllDocuments(listener);
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
