package com.venyou.venyou.View;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.venyou.venyou.Model.EventData;
import c.R;
import com.venyou.venyou.View.FacebookActivity;

import java.util.HashMap;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Home_fragment.InterfaceEventData,Attending_fragment.InterfaceAttendEventData, LiveChatFragment.OnFragmentInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageView propic;
    String uname,uid,url,uemail;
    private DatabaseReference mRef;
    FloatingActionButton chat,addEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        uname = getIntent().getExtras().getString("name");
        uemail = getIntent().getExtras().getString("email");
        uid = getIntent().getExtras().getString("id");
        View view = (View)navigationView.getHeaderView(0);
        TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
        textView_name.setText(uname);
        propic = (ImageView) view.findViewById(R.id.imageView);

        chat = (FloatingActionButton) findViewById(R.id.chat_fbutton);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,ChatActivity.class);
                startActivity(intent);
            }
        });

        addEvent = (FloatingActionButton) findViewById(R.id.addEvent_fbutton);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,AddEvent.class);
                intent.putExtra("name",uname);
                intent.putExtra("id",uid);
                intent.putExtra("email",uemail);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        uname = getIntent().getExtras().getString("name");
        uemail = getIntent().getExtras().getString("email");
        uid = getIntent().getExtras().getString("id");
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).getRef();
        mRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> event = (HashMap<String,String>)dataSnapshot.getValue();
                url = event.get("pic");
                if(url!= null){
                    Picasso.with(getApplicationContext())
                            .load(url)
                            .into(propic);
                }else{
                    url="https://firebasestorage.googleapis.com/v0/b/venyou-1ca06.appspot.com/o/users%2Favatar-1577909_960_720.png?alt=media&token=aac627f0-8fce-4ae2-91c4-87b7eaf5b852";
                    Picasso.with(getApplicationContext())
                            .load(url)
                            .into(propic);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.map) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.add_event) {
            Intent intent = new Intent(Home.this,AddEvent.class);
            intent.putExtra("name",uname);
            intent.putExtra("id",uid);
            intent.putExtra("email",uemail);
            startActivity(intent);
        }else if (id == R.id.chatbot) {
            Intent intent = new Intent(Home.this,ChatActivity.class);
            startActivity(intent);
        }else if (id == R.id.profile) {
            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
            intent.putExtra("name",uname);
            intent.putExtra("id",uid);
            intent.putExtra("url",url);
            intent.putExtra("email",uemail);
            startActivity(intent);
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,LoginActivity.class); startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
            intent.putExtra("name",uname);
            intent.putExtra("id",uid);
            intent.putExtra("url",url);
            intent.putExtra("email",uemail);
            startActivity(intent);
        }else if (id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,LoginActivity.class); startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void DisplayEventData(int position, HashMap<String, ?> eventDetails, View view, String name) {
        Intent intent = new Intent(getApplicationContext(), EventDetails.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Home.this,view ,"ActivityTransition" );
        intent.putExtra("eventData", eventDetails);
        intent.putExtra("name",uname);
        intent.putExtra("id",uid);
        intent.putExtra("email",uemail);
         Home.this.startActivity(intent, options.toBundle());
    }

    @Override
    public void onClickAddEvent() {
        Intent intent = new Intent(Home.this,AddEvent.class);
        startActivity(intent);
    }

    @Override
    public void DisplayAttendEventData(int position, HashMap<String, ?> eventDetails, View view, String name) {
        Intent intent = new Intent(getApplicationContext(), EventDetails.class);
        intent.putExtra("eventData", eventDetails);
        intent.putExtra("name",uname);
        intent.putExtra("id",uid);
        intent.putExtra("email",uemail);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void dothis(int pos, View view, String name) {

    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:{
                    return Home_fragment.newInstance();
                }
                case 1:{
                    return Attending_fragment.newInstance(uid);
                }
                case 2:{
                    return LiveChatFragment.newInstance(uname,uid);
                }
            }
            return PlaceholderFragment.newInstance(1);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
//commit