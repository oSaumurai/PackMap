package edu.osu.cse5236.group10.packmap.data.store;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;

public class LocationInfoStore extends AbstractStore {

    private static final String TAG = "LocationInfoStore";
    private static final String COLLECTION = "locations";

    private LocationInfoStore() {
        super();
    }

    private static LocationInfoStore _instance;

    public static LocationInfoStore getInstance() {
        if (_instance == null) {
            _instance = new LocationInfoStore();
        }
        return _instance;
    }

    public void addUpVote(String locationId, String userId) {
        DocumentReference dr = db.collection("locations").document(locationId);
        dr.update("upvotes", FieldValue.arrayUnion(userId));

    }
    public void addDownVote(String locationId, String userId){
        DocumentReference dr = db.collection("locations").document(locationId);
        dr.update("downvotes",FieldValue.arrayUnion(userId));
    }

    public void removeUpVote(String locationId, String userId) {
        DocumentReference dr = db.collection("locations").document(locationId);
        dr.update("upvotes", FieldValue.arrayRemove(userId));

    }
    public void removeDownVote(String locationId, String userId){
        DocumentReference dr = db.collection("locations").document(locationId);
        dr.update("downvotes",FieldValue.arrayRemove(userId));
    }

    public void addNewLocation(LocationInfo newlocation,String activityId){
        addDocument(newlocation, task->{
                    ActivityStore.getInstance().addLocation(activityId,task.getId());},
                getOnFailureListener());
        Log.d(TAG, "addLocation: updated");
    }

    public void processLocation(String locationId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        getDocument(locationId, onCompleteListener, getOnFailureListener());
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
