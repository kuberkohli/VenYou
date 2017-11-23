package com.venyou.venyou.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.venyou.venyou.R;

import java.util.HashMap;

public class EventDetails extends AppCompatActivity {

    private TextView name;
    private TextView venue;
    private TextView city;
    private TextView state;
    private HashMap<String, ?> eventDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            name = (TextView) findViewById(R.id.event_name);
            city = (TextView) findViewById(R.id.event_city);
            state = (TextView) findViewById(R.id.event_state);
            venue = (TextView) findViewById(R.id.event_venue);

            eventDetails = (HashMap<String, ?>) getIntent().getSerializableExtra("eventData");
            name.setText((String)eventDetails.get("Name"));
            city.setText((String)eventDetails.get("City"));
            state.setText((String)eventDetails.get("State"));
            venue.setText((String)eventDetails.get("Venue"));
    }

}
