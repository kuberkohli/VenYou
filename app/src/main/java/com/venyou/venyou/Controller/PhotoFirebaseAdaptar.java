package com.venyou.venyou.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.venyou.venyou.Model.Image;

import c.R;

/**
 * Created by kupal on 12/3/2017.
 */

public class PhotoFirebaseAdaptar extends FirebaseRecyclerAdapter<Image, PhotoFirebaseAdaptar.EventViewHolder> {

    private Context mContext;


    static RecyclerItemClickListener itemClickListener;

    public PhotoFirebaseAdaptar(Class<Image> modelClass, int modelLayout,
                                Class<EventViewHolder> holder, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
    }

    public interface RecyclerItemClickListener{
        void onItemClick(View view, int position, String name);
    }

    public void SetOnItemClickListner(final  RecyclerItemClickListener mItemClickListner) {
        this.itemClickListener = mItemClickListner;
    }

    @Override
    protected void populateViewHolder(EventViewHolder viewHolder, Image event, int positions) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        String url = event.getUrl();
        viewHolder.bundle.putString("url",url);
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


        public ImageView image;
        private View container;
        private Bundle bundle = new Bundle();
        public EventViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.imageView);
            container = v.findViewById(R.id.list_item_layout);

//
//            // by this we are just registering the click Event on the whole view i.e. view in our case
            container.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(itemClickListener != null){
                        if(getAdapterPosition() != RecyclerView.NO_POSITION){
                                String name = (String) bundle.get("url");
                                itemClickListener.onItemClick(v, getAdapterPosition(), name);
                            }
                        }
                    }
                }
            );
        }
    }

}

