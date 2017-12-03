package com.venyou.venyou.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.venyou.venyou.Model.EventData;
//import com.venyou.venyou.R;
import c.R;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {

    private Uri file;
    int REQUEST_CODE;
    private TextView name, email;
    private ImageView image;
    private Uri imageUri;
    private String uid,uname,uemail,url;
    Map<String,Object> pic = new HashMap<>();
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (TextView) findViewById(R.id.profile_name);
        email = (TextView) findViewById(R.id.profile_email);
        image = (ImageView) findViewById(R.id.profile_image);

        uid = (String) getIntent().getExtras().get("id");
        url = (String) getIntent().getExtras().get("url");
        uname = (String) getIntent().getExtras().get("name");
        uemail = (String) getIntent().getExtras().get("email");
        name.setText("Name: "+uname);
        email.setText("Email: "+uemail);
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).getRef();
        Picasso.with(getApplicationContext())
                .load(url)
                .into(image);
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

    public void updateProfile(View view){

            FirebaseStorage storage = FirebaseStorage.getInstance("gs://venyou-1ca06.appspot.com/");

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();
            if(imageUri != null){
                Uri file = imageUri;
                StorageReference riversRef = storageRef.child("users/"+file.getLastPathSegment());
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
                        pic.put("pic", downloadUrl.toString());
                        mRef.updateChildren(pic);

                    }
                });
            }

            Intent intent = new Intent(MyProfile.this,Home.class);
            Bundle userData = new Bundle();
            userData.putString("name", uname);
            userData.putString("email", uemail);
            userData.putString("id", uid);
            intent.putExtras(userData);
            startActivity(intent);

    }
}
