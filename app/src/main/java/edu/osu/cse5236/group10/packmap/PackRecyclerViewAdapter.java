package edu.osu.cse5236.group10.packmap;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import edu.osu.cse5236.group10.packmap.PackListFragment.OnPackListFragmentInteractionListener;
import edu.osu.cse5236.group10.packmap.data.PackListContent.PackItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PackItem} and makes a call to the
 * specified {@link PackListFragment.OnPackListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PackRecyclerViewAdapter extends RecyclerView.Adapter<PackRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "PackRecyclerViewAdapter";
    private static final ColorGenerator COLOR_GENERATOR = ColorGenerator.MATERIAL;
    private final List<PackItem> mValues;
    private final OnPackListFragmentInteractionListener mListener;

    public PackRecyclerViewAdapter(List<PackItem> items, OnPackListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String content = mValues.get(position).content;
        int color = COLOR_GENERATOR.getColor(content);
        TextDrawable image = TextDrawable.builder()
                .beginConfig().bold().toUpperCase().endConfig()
                .buildRound("" + content.charAt(0), color);
        holder.mImage.setImageDrawable(image);
        holder.mContentView.setText(content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
        public final TextView mContentView;
        public PackItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.item_image);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
