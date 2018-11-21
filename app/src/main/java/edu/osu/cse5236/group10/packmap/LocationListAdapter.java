package edu.osu.cse5236.group10.packmap;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;
import edu.osu.cse5236.group10.packmap.data.store.LocationInfoStore;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private static final String TAG = "LocationListAdapter";

    private String userId;
    public List<LocationInfo> locationList;


    public LocationListAdapter(String userId, List<LocationInfo> locationList){
        this.locationList = locationList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationInfo li = locationList.get(position);

        holder.locationName.setText(li.getName());
        holder.btnUpvote.setText(li.getUpVote());
        holder.btnDownVote.setText(li.getDownVote());
        holder.btnUpvote.setCompoundDrawablePadding(4);

        Drawable up_clicked=holder.mView.getResources().getDrawable(R.drawable.ic_up_vote_clicked);
        Drawable down_clicked=holder.mView.getResources().getDrawable(R.drawable.ic_down_vote_clicked);

        Drawable up=holder.mView.getResources().getDrawable(R.drawable.ic_up_vote);
        Drawable down=holder.mView.getResources().getDrawable(R.drawable.ic_down_vote);

        List<String> dv = li.getDownvotes();
        List<String> uv = li.getUpvotes();
        if (uv.contains(userId)) {
            holder.btnUpvote.setTextColor(Color.GREEN);
            holder.btnUpvote.setCompoundDrawablesWithIntrinsicBounds(up_clicked, null, null, null);
        }
        else {
            holder.btnUpvote.setTextColor(Color.GRAY);
            holder.btnUpvote.setCompoundDrawablesWithIntrinsicBounds(up, null, null, null);
        }


        if (dv.contains(userId)) {
            holder.btnDownVote.setTextColor(Color.RED);
            holder.btnDownVote.setCompoundDrawablesWithIntrinsicBounds(down_clicked, null, null, null);
        }

        else {
            holder.btnDownVote.setTextColor(Color.GRAY);
            holder.btnDownVote.setCompoundDrawablesWithIntrinsicBounds(down, null, null, null);
        }

        holder.btnDownVote.setOnClickListener(view -> {
            if (uv.contains(userId)) {
                uv.remove(userId);
                holder.btnUpvote.setTextColor(Color.GRAY);
                LocationInfoStore.getInstance().updateUpVote(li.getUid(),uv);
            } else if (!dv.contains(userId)) {
                dv.add(userId);
                holder.btnDownVote.setTextColor(Color.RED);
                LocationInfoStore.getInstance().updateDownVote(li.getUid(),dv);
            }
            holder.btnUpvote.setText(li.getUpVote());
            holder.btnDownVote.setText(li.getDownVote());
            notifyDataSetChanged();
        });

        holder.btnUpvote.setOnClickListener(view -> {
            if (dv.contains(userId)) {
                dv.remove(userId);
                holder.btnDownVote.setTextColor(Color.GRAY);
                LocationInfoStore.getInstance().updateDownVote(li.getUid(),dv);
            } else if (!uv.contains(userId)) {
                uv.add(userId);
                holder.btnUpvote.setTextColor(Color.GREEN);
                LocationInfoStore.getInstance().updateUpVote(li.getUid(),uv);
            }
            holder.btnUpvote.setText(li.getUpVote());
            holder.btnDownVote.setText(li.getDownVote());
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
        public ImageView mImage;
        public LinearLayout container;

        public TextView locationName;
        public Button btnUpvote;
        public Button btnDownVote;

        //public List<String> mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = (ImageView) itemView.findViewById(R.id.item_image);

            locationName = itemView.findViewById(R.id.list_location_name);
            btnUpvote = itemView.findViewById(R.id.upvote_location);
            btnDownVote = itemView.findViewById(R.id.downvote_location);
        }
    }
}
