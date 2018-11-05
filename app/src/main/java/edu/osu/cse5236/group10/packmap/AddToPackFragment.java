package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.osu.cse5236.group10.packmap.data.AddUserListContent;


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

    private SearchView mSearchView;
    private OnAddUserListFragmentInteractionListener mListener;

    public AddToPackFragment() {
        // Required empty public constructor
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
        mSearchView = view.findViewById(R.id.add_user_search);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() < 3) {
                    Toast.makeText(getActivity(),
                            "Your search query must not be less than 3 digits",
                            Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    // Search here
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
                // Search here
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
