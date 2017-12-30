package com.venyou.venyou.View;

import android.net.Uri;
import android.os.Bundle;
import android.support.transition.Fade;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.venyou.venyou.Controller.DetailsTransition;

import c.R;

public class Gallery extends AppCompatActivity implements Gallery_fragment.OnFragmentInteractionListener, image_detail_fragment.OnFragmentInteractionListener {

    private String eventName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (getIntent().getExtras() != null) {
            eventName = getIntent().getExtras().getString("name");
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, Gallery_fragment.newInstance(eventName, ""))
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onGalleryInteraction(Uri uri) {

    }

    @Override
    public void dothis(int pos, View view, String name) {
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        image_detail_fragment fragment = image_detail_fragment.newInstance(name, "");
        fragment.setSharedElementEnterTransition(new DetailsTransition());
        fragment.setEnterTransition(new android.transition.Fade());
        fragment.setExitTransition(new android.transition.Fade());
        fragment.setSharedElementReturnTransition(new DetailsTransition());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addSharedElement(image, "ImageTransition")
                .addToBackStack(null)
                .commit();
    }
}
