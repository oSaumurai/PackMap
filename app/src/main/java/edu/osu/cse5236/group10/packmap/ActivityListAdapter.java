package edu.osu.cse5236.group10.packmap;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {
    public List<ActivityInfo> memberList;
    private final PackFragment.OnPackFragmentInteractionListener mListener;

    public ActivityListAdapter(List<ActivityInfo> memberList, PackFragment.OnPackFragmentInteractionListener listener){
        this.memberList = memberList;
        this.mListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int color=generateColor();
        holder.mView.setBackgroundColor(color);
        holder.memberName.setText(memberList.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(memberList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    private int generateColor(){
        int red = ((int) (Math.random() * 200));
        int green = ((int) (Math.random() * 200));
        int blue = ((int) (Math.random() * 200));
        return Color.rgb(red, green, blue);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView memberName;
        //public List<String> mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            memberName = itemView.findViewById(R.id.list_activity_name);
        }
    }
}
