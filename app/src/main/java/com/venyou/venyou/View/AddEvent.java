package com.venyou.venyou.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.venyou.venyou.Model.EventData;
//import com.venyou.venyou.R;
import c.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    private Uri file;
    int REQUEST_CODE;
    private TextView name, city, state, venue;
    private EditText name_text, city_text, state_text, venue_text, host_text, description_text, street_text, fee_text;
    private ImageView image;
    private double longitude, latitude;
    private Uri imageUri;
    public static String event_time, event_date;
    Map<String,String> event = new HashMap<>();
    final static long today = System.currentTimeMillis() - 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name_text = (EditText) findViewById(R.id.addevent_name_text);
        host_text = (EditText) findViewById(R.id.addevent_host_text);
        description_text = (EditText) findViewById(R.id.addevent_desc_text);
        street_text = (EditText) findViewById(R.id.addevent_street_text);
        fee_text = (EditText) findViewById(R.id.addevent_fee_text);
        city_text = (EditText) findViewById(R.id.addevent_city_text);
        state_text = (EditText) findViewById(R.id.addevent_state_text);
        venue_text = (EditText) findViewById(R.id.addevent_venue_text);
        image = (ImageView) findViewById(R.id.addevent_image);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void addEvent(View view){

        final EventData eventData = new EventData();
        event.clear();
        if(host_text.getText().toString().equals("") || description_text.getText().toString().equals("") ||street_text.getText().toString().equals("") ||fee_text.getText().toString().equals("") || name_text.getText().toString().equals("") || city_text.getText().toString().equals("") || state_text.getText().toString().equals("") || venue_text.getText().toString().equals("") || imageUri == null){
            Toast.makeText(getApplicationContext(),"Please fill all the fields and select and image",Toast.LENGTH_SHORT).show();
        }else{

            String venue = street_text.getText().toString()+","+city_text.getText().toString()+","+state_text.getText().toString();
            Address myLocation = new Address(Locale.US);
            myLocation.setAddressLine(0,venue);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(myLocation.getAddressLine(0).toString(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addresses.size() > 0){
                Address address = addresses.get(0);
                longitude = address.getLongitude();
                latitude = address.getLatitude();
            }else{
                longitude = 0.0;
                latitude = 0.0;
            }


            event.put("name",name_text.getText().toString());
            event.put("city", city_text.getText().toString());
            event.put("state", state_text.getText().toString());
            event.put("venue", venue_text.getText().toString());
            event.put("id",name_text.getText().toString());
            event.put("description",description_text.getText().toString());
            event.put("host_name",host_text.getText().toString());
            event.put("fee",fee_text.getText().toString());
            event.put("street",street_text.getText().toString());
            event.put("longitude", Double.toString(longitude));
            event.put("latitude", Double.toString(latitude));
            event.put("host_rating", "4");
            String date = event_date+" "+event_time;
            if(event_time != null) event.put("time",event_time);
            else event.put("time","08:00");
            if(event_date != null) event.put("date",date);
            else event.put("date","2018-01-01 08:00");

            FirebaseStorage storage = FirebaseStorage.getInstance("gs://venyou-1ca06.appspot.com/");

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            Uri file = imageUri;
            StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getApplicationContext(),"failed : "+exception, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    event.put("image", downloadUrl.toString());
                    eventData.uploadToFirebase(event);
                    Intent intent = new Intent(AddEvent.this,Home.class);
                    intent.putExtra("name",getIntent().getExtras().getString("name"));
                    intent.putExtra("id",getIntent().getExtras().getString("id"));
                    intent.putExtra("email",getIntent().getExtras().getString("email"));
                    startActivity(intent);
                }
            });
        }

    }

    public void chooseImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            imageUri = data.getData();
            image.setImageURI(null);
            image.setImageURI(imageUri);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            event_time = Integer.toString(hourOfDay) + ":" +Integer.toString(minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
                //success
                month = month+1;
                event_date = Integer.toString(year) + "-" +Integer.toString(month) + "-" + Integer.toString(day);
        }
    }
}
