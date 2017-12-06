package com.venyou.venyou.View;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import c.R;

public class CommentsActivity extends AppCompatActivity implements CommentsFragment.OnFragmentInteractionListener, CommentDetailsFragment.OnDetailInteractionListener{

    private String eventName, uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        if (getIntent().getExtras() != null) {
            eventName = getIntent().getExtras().getString("name");
            uname = getIntent().getExtras().getString("uname");
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.comment_container, CommentsFragment.newInstance(eventName, uname))
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void dothis(int pos, View view, String name) {

    }

    @Override
    public void onDetailInteraction(Uri uri) {

    }
}
