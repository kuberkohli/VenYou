package com.venyou.venyou.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.venyou.venyou.Controller.MyFirebaseRecylerAdapter;
import com.venyou.venyou.Model.Event;
import com.venyou.venyou.Model.EventData;
import com.venyou.venyou.R;

import java.util.HashMap;


public class Home_fragment extends android.support.v4.app.Fragment {

    private static RecyclerView recyclerView;

    DatabaseReference childRef;
    private static MyFirebaseRecylerAdapter myFirebaseRecylerAdapter;
    EventData eventData;

    private OnFragmentInteractionListener mListener;

    public Home_fragment() {
        // Required empty public constructor
    }

    public static Home_fragment newInstance() {
        Home_fragment fragment = new Home_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.home_fragment, container, false);
        childRef =  FirebaseDatabase.getInstance().getReference().child("event_app").getRef();


        myFirebaseRecylerAdapter = new MyFirebaseRecylerAdapter(Event.class, R.layout.home_fragment_cardview,
                MyFirebaseRecylerAdapter.EventViewHolder.class, childRef, getContext());

        eventData = new EventData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recviewer1);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myFirebaseRecylerAdapter);

        if (eventData.getSize() == 0) {
            eventData.setAdapter(myFirebaseRecylerAdapter);
            eventData.setContext(getActivity());
            eventData.initializeDataFromCloud();
        }
        setHasOptionsMenu(true);
//
//        myFirebaseRecylerAdapter.SetOnItemClickListner(new MyFirebaseRecylerAdapter.RecyclerItemClickListener(){
//
//            @Override
//            public void onItemClick(View view, int position) {
//                final HashMap<String, ?> workoutDetails = (HashMap<String, ?>) workoutData.getItem(position);
//                normalClick(position, workoutDetails);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//
//            @Override
//            public void onOverFlowMenuClick(View v, final int position){
//                PopupMenu popup = new PopupMenu(getActivity(), v);
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        HashMap movie;
//                        switch (item.getItemId()){
//                            default:
//                                return false;
//                        }
//                    }
//                });
//            }
//
//            public void normalClick(int pos, HashMap<String, ?> mov){
//                interfaceWorkoutDataData.DisplayWorkoutData(pos, mov);
//            }
//
//            @Override
//            public void onCheckBoxClick(View v, int position) {
//
//            }
//        });

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    Interface
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
