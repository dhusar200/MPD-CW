package org.me.gcu.earthquakecw.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.me.gcu.earthquakecw.Adapter.MyEQuakeListRecyclerViewAdapter;
import org.me.gcu.earthquakecw.EarthQuake;
import org.me.gcu.earthquakecw.EarthQuakeActivity;
import org.me.gcu.earthquakecw.R;
import org.me.gcu.earthquakecw.RetrieveData;
import org.me.gcu.earthquakecw.UI.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A fragment representing a list of Items.
 */
public class EQuakeListFragment extends Fragment implements MyEQuakeListRecyclerViewAdapter.OnNoteListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<EarthQuake> quakeList;
    private RetrieveData getXML = new RetrieveData();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EQuakeListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EQuakeListFragment newInstance(int columnCount) {
        EQuakeListFragment fragment = new EQuakeListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        quakeList = getArguments().getParcelableArrayList("quakeList");
//        Log.e("MyTag77",((MainActivity) getActivity()).test());

        //if (((MainActivity) getActivity()).getData() != null)
        {
            quakeList = ((MainActivity) getActivity()).getData();
            Log.e("MyTag", "Passed List");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyEQuakeListRecyclerViewAdapter(quakeList, this));
        }
        return view;
    }

    @Override
    public void onNoteClick(int position) {
        Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
        i.putExtra("Position" , position);

        Bundle bundle = new Bundle();
        bundle.putParcelable("earthQuake", quakeList.get(position));
        i.putExtras(bundle);
        startActivity(i);

    }
}