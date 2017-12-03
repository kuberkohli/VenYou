package com.venyou.venyou.View;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.venyou.venyou.Controller.PhotoFirebaseAdaptar;
import com.venyou.venyou.Model.Image;
import com.venyou.venyou.Model.ImageData;

import c.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Gallery_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Gallery_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Gallery_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ImageData observerData;
    public PhotoFirebaseAdaptar recyclerViewAdapter;
    DatabaseReference childRef;
    private static PhotoFirebaseAdaptar myFirebaseRecylerAdapter;
    private static RecyclerView recyclerView;
    String eventName;
    int layoutType = 2;

    private LinearLayoutManager lm;

    private OnFragmentInteractionListener mListener;

    public Gallery_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Gallery_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Gallery_fragment newInstance(String param1, String param2) {
        Gallery_fragment fragment = new Gallery_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View view = inflater.inflate(R.layout.fragment_gallery_fragment, container, false);
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        childRef = FirebaseDatabase.getInstance().getReference().child("photbox").child(eventName).getRef();

        myFirebaseRecylerAdapter = new PhotoFirebaseAdaptar(Image.class, R.layout.photobox_cardview,
                PhotoFirebaseAdaptar.EventViewHolder.class, childRef, getActivity());

        observerData = new ImageData(eventName);
        recyclerView = (RecyclerView) view.findViewById(R.id.imageRecycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(myFirebaseRecylerAdapter);

        if (observerData.getSize() == 0) {
            observerData.setAdapter(myFirebaseRecylerAdapter);
            observerData.setContext(getActivity());
            observerData.initializeDataFromCloud();
        }

        myFirebaseRecylerAdapter.SetOnItemClickListner(new PhotoFirebaseAdaptar.RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String name) {
                if (observerData != null) {
//                    final HashMap<String, ?> eventDetails = (HashMap<String, ?>) observerData.getItem(name);
                    mListener.dothis(position,view,name);
//                    normalClick(position, view, name);
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void dothis(int pos, View view, String name);
    }

}
