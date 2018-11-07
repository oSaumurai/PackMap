package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import edu.osu.cse5236.group10.packmap.data.AddUserListContent;
import edu.osu.cse5236.group10.packmap.data.DataUtils;
import edu.osu.cse5236.group10.packmap.data.model.User;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddToPackFragment.OnAddUserListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddToPackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddToPackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "AddToPackFragment";

    private UserStore mUserStore;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private OnAddUserListFragmentInteractionListener mListener;

    public AddToPackFragment() {
        mUserStore = UserStore.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddToPackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddToPackFragment newInstance(String param1, String param2) {
        AddToPackFragment fragment = new AddToPackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_pack, container, false);
        mRecyclerView = view.findViewById(R.id.add_user_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mSearchView = view.findViewById(R.id.add_user_search);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() < 10) {
                    Toast.makeText(getActivity(),
                            R.string.not_valid_user,
                            Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    mUserStore.processUser(s, task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                AddUserListContent.createAddUserItem(
                                        DataUtils.getObject(document, User.class));
                                AddUserRecyclerViewAdapter viewAdapter =
                                        new AddUserRecyclerViewAdapter(
                                                AddUserListContent.ITEMS, mListener);
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        mRecyclerView.setAdapter(viewAdapter);
                                    }
                                });
                            } else {
                                Log.d(TAG, "User not exists");
                                Toast.makeText(getActivity(),
                                        R.string.not_valid_user,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Search here
                if (newText.length() >= 10) {
                    mUserStore.processUser(newText, task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                AddUserListContent.createAddUserItem(
                                        DataUtils.getObject(document, User.class));
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        AddUserRecyclerViewAdapter viewAdapter =
                                                new AddUserRecyclerViewAdapter(
                                                        AddUserListContent.ITEMS, mListener);
                                        Log.d(TAG, "Where does this fail.");
                                        mRecyclerView.setAdapter(viewAdapter);
                                    }
                                });
                            } else {
                                Log.d(TAG, "User not exists");
                                Toast.makeText(getActivity(),
                                        R.string.not_valid_user,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
                }
                return true;
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddUserListFragmentInteractionListener) {
            mListener = (OnAddUserListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddUserListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AddUserListContent.AddUserItem item);
    }
}
