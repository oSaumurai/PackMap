package edu.osu.cse5236.group10.packmap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.osu.cse5236.group10.packmap.PackListFragment.OnPackListFragmentInteractionListener;
import edu.osu.cse5236.group10.packmap.data.AddUserListContent;
import edu.osu.cse5236.group10.packmap.data.PackListContent.PackItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PackItem} and makes a call to the
 * specified {@link OnPackListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AddUserRecyclerViewAdapter extends RecyclerView.Adapter<AddUserRecyclerViewAdapter.AddUserViewHolder> {

    private static final String TAG = "PackRecyclerViewAdapter";
    private final List<AddUserListContent.AddUserItem> mValues;
    private final AddToPackFragment.OnAddUserListFragmentInteractionListener mListener;

    public AddUserRecyclerViewAdapter(List<AddUserListContent.AddUserItem> items, AddToPackFragment.OnAddUserListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public AddUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new AddUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddUserViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

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

    public class AddUserViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public AddUserListContent.AddUserItem mItem;

        public AddUserViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
