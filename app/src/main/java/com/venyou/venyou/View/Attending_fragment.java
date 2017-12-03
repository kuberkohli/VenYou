package com.venyou.venyou.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.venyou.venyou.Controller.Attending_FirebaseRecylerAdapter;
import com.venyou.venyou.Controller.MyFirebaseRecylerAdapter;
import com.venyou.venyou.Model.Attending_EventData;
import com.venyou.venyou.Model.Event;
import com.venyou.venyou.Model.EventData;
//import com.venyou.venyou.R;
import c.R;
import java.util.HashMap;


public class Attending_fragment extends android.support.v4.app.Fragment {

    private static RecyclerView recyclerView;
    private static String uid;
    DatabaseReference childRef;
    private static Attending_FirebaseRecylerAdapter myFirebaseRecylerAdapter;
    Attending_EventData eventData;

    private OnFragmentInteractionListener mListener;

    public Attending_fragment() {
        // Required empty public constructor
    }

    public static Attending_fragment newInstance(String id) {
        Attending_fragment fragment = new Attending_fragment();
        uid = id;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public interface InterfaceEventData{
        void DisplayEventData(int position, HashMap<String, ?> eventDetails, View view, String name);
        void onClickAddEvent();
    }

    InterfaceEventData interfaceEventData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.attending_fragment, container, false);
        childRef =  FirebaseDatabase.getInstance().getReference().child("event_app").getRef();

//        interfaceEventData = (InterfaceEventData) view.getContext();
        myFirebaseRecylerAdapter = new Attending_FirebaseRecylerAdapter(Event.class, R.layout.home_fragment_cardview,
                Attending_FirebaseRecylerAdapter.EventViewHolder.class, childRef, getContext(),uid);

        eventData = new Attending_EventData(uid);
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

        myFirebaseRecylerAdapter.SetOnItemClickListner(new Attending_FirebaseRecylerAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String name) {
                final HashMap<String, ?> eventDetails = (HashMap<String, ?>) eventData.getItem(name);
                normalClick(position, eventDetails, view,name);
            }
        });
        FloatingActionButton chat = (FloatingActionButton) view.findViewById(R.id.chat_button);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton addEvent = (FloatingActionButton) view.findViewById(R.id.addEvent_button);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceEventData.onClickAddEvent();
            }
        });

        return view;

    }

    public void normalClick(int pos, HashMap<String, ?> eventDetails, View view, String name){
//        interfaceEventData.DisplayEventData(pos, eventDetails, view, name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
