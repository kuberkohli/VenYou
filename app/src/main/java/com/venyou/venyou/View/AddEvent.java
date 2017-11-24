package com.venyou.venyou.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.venyou.venyou.Model.EventData;
import com.venyou.venyou.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    private Uri file;
    int REQUEST_CODE;
    private TextView name, city, state, venue;
    private EditText name_text, city_text, state_text, venue_text;
    private ImageView image;
    private Uri imageUri;
    Map<String,String> event = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name_text = (EditText) findViewById(R.id.addevent_name_text);
        city_text = (EditText) findViewById(R.id.addevent_city_text);
        state_text = (EditText) findViewById(R.id.addevent_state_text);
        venue_text = (EditText) findViewById(R.id.addevent_venue_text);
        image = (ImageView) findViewById(R.id.addevent_image);
    }

    public void addEvent(View view){

        final EventData eventData = new EventData();
        event.clear();
        if(name_text.getText().toString().equals("") || city_text.getText().toString().equals("") || state_text.getText().toString().equals("") || venue_text.getText().toString().equals("") || imageUri == null){
            Toast.makeText(getApplicationContext(),"Please fill all the fields and select and image",Toast.LENGTH_SHORT).show();
        }else{
            event.put("Name",name_text.getText().toString());
            event.put("City", city_text.getText().toString());
            event.put("State", state_text.getText().toString());
            event.put("Venue", venue_text.getText().toString());
            event.put("id",name_text.getText().toString());

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
                    event.put("pic", downloadUrl.toString());
                    eventData.uploadToFirebase(event);
                }
            });

            Intent intent = new Intent(AddEvent.this,Home.class);
            startActivity(intent);
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
}
