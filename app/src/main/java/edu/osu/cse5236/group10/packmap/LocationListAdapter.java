package edu.osu.cse5236.group10.packmap;

import android.graphics.Color;
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
        //ActivityStore.getInstance().updateList(activityId, locationList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationInfo li = locationList.get(position);

        holder.locationName.setText(li.getName());
        holder.score.setText(li.getScore());

        List<String> dv = li.getDownvotes();
        List<String> uv = li.getUpvotes();

        if (uv.contains(userId))
            holder.btnUpvote.setBackgroundColor(Color.GREEN);
        else
            holder.btnUpvote.setBackgroundColor(Color.GRAY);

        if (dv.contains(userId))
            holder.btnDownVote.setBackgroundColor(Color.RED);
        else
            holder.btnDownVote.setBackgroundColor(Color.GRAY);

        holder.btnDownVote.setOnClickListener(view -> {
            if (uv.contains(userId)) {
                uv.remove(userId);
                holder.btnUpvote.setBackgroundColor(Color.GRAY);
            } else if (!dv.contains(userId)) {
                dv.add(userId);
                holder.btnDownVote.setBackgroundColor(Color.RED);
            }
            //li.updateScore();
            update();
            holder.score.setText(li.getScore());
            notifyDataSetChanged();
        });

        holder.btnUpvote.setOnClickListener(view -> {
            if (dv.contains(userId)) {
                dv.remove(userId);
                holder.btnDownVote.setBackgroundColor(Color.GRAY);
            } else if (!uv.contains(userId)) {
                uv.add(userId);
                holder.btnUpvote.setBackgroundColor(Color.GREEN);
            }
            update();
            holder.score.setText(li.getScore());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+ locationList.size());
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
