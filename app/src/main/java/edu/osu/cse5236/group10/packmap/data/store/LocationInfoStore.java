package edu.osu.cse5236.group10.packmap.data.store;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
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

    public void addNewLocation(LocationInfo newlocation){
        CollectionReference cr = db.collection(COLLECTION);
        Map map = new HashMap<>();
        map.put("name",newlocation.getName());
        map.put("Coordinates",newlocation.getCoordinates());
        map.put("upVote",newlocation.getUpvotes());
        map.put("downVote",newlocation.getDownvotes());
        cr.add(map);
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
