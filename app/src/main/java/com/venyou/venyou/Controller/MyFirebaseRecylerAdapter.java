package com.venyou.venyou.Controller; //change the package name to your project's package name

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.venyou.venyou.Model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import c.R;

public class MyFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<Event, MyFirebaseRecylerAdapter.EventViewHolder> {

    private Context mContext;


    static RecyclerItemClickListener itemClickListener;

    public MyFirebaseRecylerAdapter(Class<Event> modelClass, int modelLayout,
                                    Class<EventViewHolder> holder, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
    }

    public interface RecyclerItemClickListener{
        void onItemClick(View view, int position, String name);
    }

    public void SetOnItemClickListner(final  MyFirebaseRecylerAdapter.RecyclerItemClickListener mItemClickListner) {
        this.itemClickListener = mItemClickListner;
    }

    @Override
    protected void populateViewHolder(EventViewHolder viewHolder, Event event, int positions) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields

        String name = (String) event.getName();
        String state = (String) event.getState();
        String url = (String) event.getImage();
        String eventdate = (String) event.getDate();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        String currentDate = df.format(c.getTime());
        if (currentDate.compareTo(eventdate) > 0) {
            viewHolder.event_date_check.setVisibility(View.VISIBLE);
        }else{
            viewHolder.event_date_check.setVisibility(View.INVISIBLE);
        }

        viewHolder.bundle.putString("name",name);
        viewHolder.name.setText(name);
        viewHolder.state.setText("State :"+ state);
        Picasso.with(mContext).load(url).into(viewHolder.image);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public Event getItem(int position) {
////        return super.getItem(position);
//        return super.getItem(position);
//    }


    //TODO: Populate ViewHolder and add listeners.
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView state;
        public ImageView image;
        public ImageView event_date_check;
        private View container;
        private Bundle bundle = new Bundle();
        public EventViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.event_name);
            state = (TextView) v.findViewById(R.id.event_state);
            image = (ImageView) v.findViewById(R.id.imageView);
            event_date_check = (ImageView) v.findViewById(R.id.event_date_check);
            event_date_check.setVisibility(View.INVISIBLE);
            container = v.findViewById(R.id.list_item_layout);

            // by this we are just registering the click Event on the whole view i.e. view in our case
            container.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(itemClickListener != null){
                        if(getAdapterPosition() != RecyclerView.NO_POSITION){
                            if(bundle.get("name") != null){
                                String name = (String) bundle.get("name");
                                itemClickListener.onItemClick(v, getAdapterPosition(), name);
                            }
                        }
                    }
                }
            });
        }
    }

}

