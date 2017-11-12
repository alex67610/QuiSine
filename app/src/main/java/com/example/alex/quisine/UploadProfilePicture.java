package com.example.alex.quisine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadProfilePicture extends AppCompatActivity{

    Button chooseImg, uploadImg,uploadCancel;
    ImageView imgView;
    int PICK_IMG_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://quisine-c8d9c.appspot.com");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        chooseImg = (Button) findViewById(R.id.chooseImg);
        uploadImg = (Button) findViewById(R.id.uploadImg);
        uploadCancel = (Button) findViewById(R.id.uploadCancel);
        imgView = (ImageView) findViewById(R.id.imgView);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");

        uploadCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(UploadProfilePicture.this, SettingsActivity.class);
                startActivity(intentBack);
            }
        });

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMG_REQUEST);
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    pd.show();

                    StorageReference childRef = storageRef.child(uid);

                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(UploadProfilePicture.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            Intent intentBackHome = new Intent(UploadProfilePicture.this, MainActivity.class);
                            startActivity(intentBackHome);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(UploadProfilePicture.this, "Upload failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(UploadProfilePicture.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imgView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
