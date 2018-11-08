package edu.osu.cse5236.group10.packmap;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;
import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    public List<LocationInfo> locationList;
    private final LocationListFragment.OnLocationListFragmentInteractionListener mListener;


    public LocationListAdapter(List<LocationInfo> locationList, LocationListFragment.OnLocationListFragmentInteractionListener listener){
        this.locationList = locationList;
        this.mListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.locationName.setText(locationList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView locationName;
        //public List<String> mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            locationName = itemView.findViewById(R.id.list_location_name);
        }
    }
}
