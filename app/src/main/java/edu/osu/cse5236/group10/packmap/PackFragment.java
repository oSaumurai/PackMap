package edu.osu.cse5236.group10.packmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.osu.cse5236.group10.packmap.data.DataUtils;
import edu.osu.cse5236.group10.packmap.data.PackListContent;
import edu.osu.cse5236.group10.packmap.data.model.Group;
import edu.osu.cse5236.group10.packmap.data.store.GroupStore;
import edu.osu.cse5236.group10.packmap.data.PackListContent.PackItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PackFragment extends Fragment {

    private static final String TAG = "PackFragment";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private GroupStore mGroupStore;
    private RecyclerView mRecyclerView;
    private Activity packActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PackFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PackFragment newInstance(int columnCount) {
        PackFragment fragment = new PackFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate() called");

        setHasOptionsMenu(true);
        packActivity = getActivity();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");

        View view = inflater.inflate(R.layout.fragment_pack_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mGroupStore = GroupStore.getInstance();
            mGroupStore.getGroups(new GetGroupsOnCompleteListener());
            mRecyclerView.setAdapter(new PackRecyclerViewAdapter(PackListContent.ITEMS, mListener));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pack_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.modify_account:
                startActivity(new Intent(packActivity, AccountSettingActivity.class));
                return true;
        }

        return false;
    }

    public class GetGroupsOnCompleteListener implements OnCompleteListener<QuerySnapshot> {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                List<Group> groups = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    Group group = DataUtils.getObject(document, Group.class);
                    groups.add(group);
                }
                List<PackItem> temp = new ArrayList<>(PackListContent.ITEMS);
                PackListContent.ITEMS.clear();
                Stream.of(groups)
                        .map(Group::getName)
                        .mapIndexed(PackListContent::createPackItem)
                        .forEach(PackListContent::addItem);
                PackListContent.ITEMS.addAll(temp);
                Log.d(TAG, "Size of items: " + PackListContent.ITEMS.size());
                Log.d(TAG, "Last item: " + PackListContent.ITEMS.get(PackListContent.ITEMS.size() - 1));
                PackRecyclerViewAdapter viewAdapter = new PackRecyclerViewAdapter(PackListContent.ITEMS, mListener);
                // notifyDataSetChanged() must run in the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        viewAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(viewAdapter);
                    }
                });

            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG,  "onAttach() called");

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "onDetach() called");

        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PackItem item);
    }
}
