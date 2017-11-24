package com.venyou.venyou.Model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.venyou.venyou.Controller.MyFirebaseRecylerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kuberkohli on 11/21/17.
 */

public class EventData implements Serializable{

    ArrayList<Map<String,?>> eventlist;
    DatabaseReference mRef;
    MyFirebaseRecylerAdapter myFirebaseRecylerAdapter;
    Context mContext;

    public void setAdapter(MyFirebaseRecylerAdapter mAdapter) {
        myFirebaseRecylerAdapter = mAdapter;
    }

    public DatabaseReference getFireBaseRef(){
        return mRef;
    }

    public void setContext(Context context){
        mContext = context;
    }

    public List<Map<String, ?>> getworkoutList() {
        return eventlist;
    }

    public int getSize(){
        return eventlist.size();
    }

    public HashMap getItem(String s){
        for(int i=0;i<eventlist.size();i++){
            HashMap event = (HashMap)eventlist.get(i);
            String name = (String)event.get("Name");
            if (name.equals(s)){
                return event;
                }
        }
       return null;
    }


    public void onItemRemovedFromCloud(HashMap item){
        int position = -1;
        String id=(String)item.get("id");
        for(int i=0;i<eventlist.size();i++){
            HashMap event = (HashMap)eventlist.get(i);
            String mid = (String)event.get("id");
            if(mid.equals(id)){
                position= i;
                break;
            }
        }
        if(position != -1){
            eventlist.remove(position);
        }
    }

    public void onItemAddedToCloud(HashMap item){
        int insertPosition = 0;
        String id= item.get("id").toString();
        for(int i=0;i<eventlist.size();i++){
            HashMap event = (HashMap)eventlist.get(i);
            String mid = (String)event.get("id");
            if(mid.equals(id)){
                return;
            }
            if(mid.compareTo(id)<0){
                insertPosition=i+1;
            }else{
                break;
            }
        }
        eventlist.add(insertPosition,item);
    }

    public void onItemUpdatedToCloud(HashMap item){
        String id=(String)item.get("id");
        for(int i=0;i<eventlist.size();i++){
            HashMap event = (HashMap)eventlist.get(i);
            String mid = (String)event.get("id");
            if(mid.equals(id)){
                eventlist.remove(i);
                eventlist.add(i,item);
                break;
            }
        }

    }

    public void initializeDataFromCloud() {
        eventlist.clear();
        mRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.d("MyTest: OnChildAdded", dataSnapshot.toString());
                HashMap<String,String> event = (HashMap<String,String>)dataSnapshot.getValue();
                onItemAddedToCloud(event);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.d("MyTest: OnChildChanged", dataSnapshot.toString());
                HashMap<String,String> event = (HashMap<String,String>)dataSnapshot.getValue();
                onItemUpdatedToCloud(event);
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("MyTest: OnChildRemoved", dataSnapshot.toString());
                HashMap<String,String> event = (HashMap<String,String>)dataSnapshot.getValue();
                onItemRemovedFromCloud(event);
            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void uploadToFirebase(Map item){
        if(item != null){
            String name = item.get("Name").toString();
            mRef.child(name).setValue(item);
        }
    }

    public EventData(){
        eventlist = new ArrayList<Map<String,?>>();
        mRef = FirebaseDatabase.getInstance().getReference().child("event_app").getRef();
        myFirebaseRecylerAdapter = null;
        mContext = null;
    }

}
