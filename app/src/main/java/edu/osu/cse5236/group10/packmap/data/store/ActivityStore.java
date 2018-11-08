package edu.osu.cse5236.group10.packmap.data.store;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.MapFragment;
import edu.osu.cse5236.group10.packmap.PackFragment;
import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;
import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;

public class ActivityStore extends AbstractStore {

    private static final String TAG = "ActivityStore";
    private static final String COLLECTION = "activities";

    private ActivityStore() {
        super();
    }

    private static ActivityStore instance;

    public static ActivityStore getInstance() {
        if (instance == null) {
            instance = new ActivityStore();
        }
        return instance;
    }


    public void addLocation(String activityId, LocationInfo newlocation){
        DocumentReference dr = db.collection("activities").document(activityId);
        Map map=new HashMap<>();
        map.put("name",newlocation.getName());
        map.put("Coordinates",newlocation.getCoordinates());
        map.put("upVote",newlocation.getUpvotes());
        map.put("downVote",newlocation.getDownvotes());
        Map<String, Map<String, Object>> temp=new HashMap<>();
        temp.put(activityId, map);
        dr.update("selectedLocations", FieldValue.arrayUnion(map));
        Log.d(TAG, "addLocation: updated");
    }

    public void updateList(String activityId, List<LocationInfo> newlocations) {
        DocumentReference dr = db.collection("activities").document(activityId);
        List<Map<String, Object>> map = new ArrayList<>();

        for (int i = 0; i < newlocations.size(); ++i) {
            LocationInfo li = newlocations.get(i);
            Map<String, Object> temp = new HashMap<>();

            temp.put("name",li.getName());
            temp.put("Coordinates",li.getCoordinates());
            temp.put("upVote",li.getUpvotes());
            temp.put("downVote",li.getDownvotes());
            map.add(temp);
        }

        dr.update("selectedLocations", map);
    }

    public void removeFromlist(int index){
        DocumentReference dr = db.collection("activities").document("ccc");
        dr.update("selectedLocations", FieldValue.arrayRemove(index));
    }

    public void getActivityById(String activityId,OnCompleteListener<QuerySnapshot> listener){
        db.collection(COLLECTION)
                .whereEqualTo("selectedLocations", activityId)
                .get()
                .addOnCompleteListener(listener);
    }

    public void getAllActivity(OnCompleteListener<QuerySnapshot> listener){
        db.collection(COLLECTION)
                .get()
                .addOnCompleteListener(listener);
    }


    public void addNewActivity(String newActivity,String info ,String groupUID) {
        List<LocationInfo> list=new ArrayList<>();
        ActivityInfo newAct=new ActivityInfo(newActivity,info,false, list);

        addDocument(newAct, task->{
            GroupStore.getInstance().addActivity(groupUID,task.getId());},
                getOnFailureListener());
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
