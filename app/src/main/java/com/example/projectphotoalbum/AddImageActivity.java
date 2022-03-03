package com.example.projectphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText img_title, img_description;
    private Button saveImg_btn;
    private ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;
    private Bitmap selectedImage;
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Image");
        setContentView(R.layout.activity_add_image);

        registerActivityForSelectImage();
        imageView       = findViewById(R.id.add_img);
        img_title       = findViewById(R.id.img_title);
        img_description = findViewById(R.id.img_description);
        saveImg_btn     = findViewById(R.id.img_btn);

        imageView.setOnClickListener(view -> {

            if (ContextCompat.checkSelfPermission(AddImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AddImageActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImage.launch(intent);
            }
        });

        saveImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (selectedImage == null){
                    Toast.makeText(AddImageActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }else {

                    String title                                = img_title.getText().toString();
                    String description                          = img_description.getText().toString();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    scaledImage                                 = smallerImage(selectedImage,300);
                    scaledImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
                    byte[] image                                = byteArrayOutputStream.toByteArray();

                    Intent intent = new Intent();
                    intent.putExtra("title",title);
                    intent.putExtra("description",description);
                    intent.putExtra("image",image);
                    setResult(RESULT_OK,intent);
                    finish();
                }


            }
        });
    }

    public void registerActivityForSelectImage(){

        activityResultLauncherForSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            int resultCode = result.getResultCode();
            Intent data    = result.getData();

            // if user selected an image this will be true
            if (resultCode == RESULT_OK && data != null){

                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    imageView.setImageBitmap(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectImage.launch(intent);
        }
    }

    public Bitmap smallerImage (Bitmap img, int maxSize){
        int width   = img.getWidth();
        int height  = img.getHeight();

        float ratio = (float) width/ (float) height;

        if (ratio > 1){
            width = maxSize;
            height = (int)(width / ratio);

        }else {
            height = maxSize;
            width = (int)(height * ratio);
        }

        return Bitmap.createScaledBitmap(img,width,height,true);
    }
}