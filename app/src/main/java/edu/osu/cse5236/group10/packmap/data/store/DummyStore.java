package edu.osu.cse5236.group10.packmap.data.store;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DummyStore extends AbstractStore {

    private static final String TAG = "DummyStore";
    private static final String COLLECTION = "dummy";

    public void addDummy(String group) {
        Map<String, Object> dummy = new HashMap<>();
        dummy.put("group", group);
        addDocument(COLLECTION, dummy);
    }

    public void updateDummies(OnCompleteListener<QuerySnapshot> listener) {
        getAllDocuments(COLLECTION, listener);
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
