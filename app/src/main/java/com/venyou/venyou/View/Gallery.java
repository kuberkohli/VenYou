package com.venyou.venyou.View;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
//        Intent intent = new Intent(getApplicationContext(), ImageDetails.class);
//        intent.putExtra("url", name);
//        startActivity(intent);
    }

    @Override
    public void dothis(int pos, View view, String name) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, image_detail_fragment.newInstance(name, ""))
                .commit();
    }
}
