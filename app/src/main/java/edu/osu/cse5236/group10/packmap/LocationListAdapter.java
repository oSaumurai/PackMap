package edu.osu.cse5236.group10.packmap;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;
import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;
import edu.osu.cse5236.group10.packmap.data.store.ActivityStore;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private static final String TAG = "LocationListAdapter";

    private String userId;
    private String activityId;
    public List<LocationInfo> locationList;
    private final LocationListFragment.OnLocationListFragmentInteractionListener mListener;


    public LocationListAdapter(String activityId, String userId, List<LocationInfo> locationList, LocationListFragment.OnLocationListFragmentInteractionListener listener){
        this.locationList = locationList;
        this.mListener=listener;
        this.userId = userId;
        this.activityId = activityId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);

        return new ViewHolder(v);
    }

    private void update() {
        ActivityStore.getInstance().updateList(activityId, locationList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationInfo li = locationList.get(position);

        holder.locationName.setText(li.getName());
        holder.score.setText(li.getScore());

        holder.btnDownVote.setOnClickListener(view -> {
            List<String> dv = li.getDownvotes();
            List<String> uv = li.getUpvotes();
            if (uv.contains(userId))
                uv.remove(userId);
            else if (!dv.contains(userId))
                dv.add(userId);

            li.updateScore();
            update();
            holder.score.setText(li.getScore());
            notifyDataSetChanged();
        });

        holder.btnUpvote.setOnClickListener(view -> {
            List<String> uv = li.getUpvotes();
            List<String> dv = li.getDownvotes();
            if (dv.contains(userId))
                dv.remove(userId);
            else if (!uv.contains(userId))
                uv.add(userId);

            li.updateScore();
            update();
            holder.score.setText(li.getScore());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView locationName;
        public TextView score;
        public Button btnUpvote;
        public Button btnDownVote;

        //public List<String> mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            locationName = itemView.findViewById(R.id.list_location_name);
            score = itemView.findViewById(R.id.list_score);
            btnUpvote = itemView.findViewById(R.id.upvote_location);
            btnDownVote = itemView.findViewById(R.id.downvote_location);
        }
    }
}
