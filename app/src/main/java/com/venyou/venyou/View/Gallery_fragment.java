package com.venyou.venyou.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.venyou.venyou.Controller.PhotoFirebaseAdaptar;
import com.venyou.venyou.Model.Image;
import com.venyou.venyou.Model.ImageData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import c.R;

public class Gallery_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button button, chooseImage;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ImageData observerData;
    public PhotoFirebaseAdaptar recyclerViewAdapter;
    DatabaseReference childRef;
    private static PhotoFirebaseAdaptar myFirebaseRecylerAdapter;
    private static RecyclerView recyclerView;
    String eventName;
    int layoutType = 2, size;
    int REQUEST_CODE;
    private Uri imageUri;
    DatabaseReference mRef;
    Map<String, Object> pic = new HashMap<>();

    private LinearLayoutManager lm;

    private OnFragmentInteractionListener mListener;

    public Gallery_fragment() {
        // Required empty public constructor
    }

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        eventName = mParam1;
//        eventName = "Adele Concert";

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
                    mListener.dothis(position, view, name);
//                    normalClick(position, view, name);
                }
            }
        });

        mRef = FirebaseDatabase.getInstance().getReference().child("photbox").child(eventName).getRef();
        if (mRef != null) {
            mRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, String> event = (HashMap<String, String>) dataSnapshot.getValue();
                    size = event.size();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        button = (Button) view.findViewById(R.id.photoBox_image_upload);
        chooseImage = (Button) view.findViewById(R.id.choose_image_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a storage reference from our app
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://venyou-1ca06.appspot.com/");
                StorageReference storageRef = storage.getReference();
                if (imageUri != null) {
                    Uri file = imageUri;
                    StorageReference riversRef = storageRef.child("photobox/" + file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Snackbar.make(getView(), "Failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            size = size + 1;
                            String item = "item"+Integer.toString(size);
                            pic.put("id",item);
                            pic.put("url", downloadUrl.toString());
                            mRef.child(item).updateChildren(pic);

                        }
                    });
                }else{
                    Toast.makeText(getContext(),"Please choose an image to upload.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),REQUEST_CODE);
            }
        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageUri = data.getData();

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGalleryInteraction(uri);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onGalleryInteraction(Uri uri);

        void dothis(int pos, View view, String name);
    }

}
