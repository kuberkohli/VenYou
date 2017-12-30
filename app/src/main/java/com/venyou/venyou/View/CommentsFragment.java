package com.venyou.venyou.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.venyou.venyou.Controller.CommentFirebaseAdaptar;
import com.venyou.venyou.Model.CommentData;
import com.venyou.venyou.Model.Image;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import c.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentData observerData;
    DatabaseReference childRef;
    private static CommentFirebaseAdaptar myFirebaseRecylerAdapter;
    private static RecyclerView recyclerView;
    private TextView postButton;
    private EditText editText;
    String eventName;
    int layoutType = 2;

    private LinearLayoutManager lm;


    private OnFragmentInteractionListener mListener;

    public CommentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentsFragment newInstance(String param1, String param2) {
        CommentsFragment fragment = new CommentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        postButton = (TextView)view.findViewById(R.id.post);
        editText = (EditText) view.findViewById(R.id.postText);
        eventName = mParam1;


        childRef = FirebaseDatabase.getInstance().getReference().child("Comment").child(eventName).getRef();

        myFirebaseRecylerAdapter = new CommentFirebaseAdaptar(Image.class, R.layout.comment_cardview,
                CommentFirebaseAdaptar.EventViewHolder.class, childRef, getActivity());

        observerData = new CommentData(eventName);
        recyclerView = (RecyclerView) view.findViewById(R.id.commentRecycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myFirebaseRecylerAdapter);

        if (observerData.getSize() == 0) {
            observerData.setAdapter(myFirebaseRecylerAdapter);
            observerData.setContext(getActivity());
            observerData.initializeDataFromCloud();
        }

        myFirebaseRecylerAdapter.SetOnItemClickListner(new CommentFirebaseAdaptar.RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String name) {
                if (observerData != null) {
//                    final HashMap<String, ?> eventDetails = (HashMap<String, ?>) observerData.getItem(name);
                    mListener.dothis(position,view,name);
//                    normalClick(position, view, name);
                }
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value  = String.valueOf(editText.getText());
                if(value.length()>1) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comment");
                    FirebaseAuth mauth = FirebaseAuth.getInstance();
                    Date timestamp = new Date();
                    Calendar calendar = new GregorianCalendar();
                    final int day = calendar.get(Calendar.DAY_OF_WEEK);
                    final SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                    String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
                    Log.d("Comment check",eventName);
                    final String userid = mauth.getCurrentUser().getUid();
                    final DatabaseReference userdb = ref.child(eventName).child(String.valueOf(Integer.MAX_VALUE - timestamp.getTime() % 1000000000));
                    userdb.child("id").setValue(String.valueOf(Integer.MAX_VALUE - timestamp.getTime() % 1000000000));
                    userdb.child("url").setValue(mParam2 + ": " +String.valueOf(editText.getText()));
                }else{
                    Snackbar.make(getView(),"Kindly enter a comment in the text field.",Snackbar.LENGTH_SHORT).show();
                }
                editText.setText("");
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

    String getDay(int i){
        String dayString = "";
        switch (i){
            case 1: i=1;
                dayString = "Sunday";
                break;
            case 2: i=1;
                dayString = "Monday";
                break;
            case 3: i=1;
                dayString = "Tuesday";
                break;
            case 4: i=1;
                dayString = "Wednesday";
                break;
            case 5: i=1;
                dayString = "Thursday";
                break;
            case 6: i=1;
                dayString = "Friday";
                break;
            case 7: i=1;
                dayString = "Saturday";
                break;
            default:dayString = "DayTime";
                break;
        }
        return  dayString;
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
