package com.example.projectphotoalbum;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText update_title, update_description;
    private MaterialButton update_btn;

    private String title, description;
    private  int id;
    private byte[] image;

    private ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;
    private Bitmap selectedImage;
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Update Image");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);

        imageView          = findViewById(R.id.update_img);
        update_title       = findViewById(R.id.update_title);
        update_description = findViewById(R.id.update_description);
        update_btn         = findViewById(R.id.update_btn);

        registerActivityForUpdateImage();
        Intent intent      = getIntent();
            id             = intent.getIntExtra("id",-1);
            title          = intent.getStringExtra("title");
            description    = intent.getStringExtra("description");
            image          = intent.getByteArrayExtra("image");

            update_title.setText(title);
            update_description.setText(description);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImage.launch(intent);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatedDate();
            }
        });
    }

    public void registerActivityForUpdateImage(){

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

    public void updatedDate (){

        if (id == -1){
            Toast.makeText(this, "There is a problem the id is -1", Toast.LENGTH_SHORT).show();

        }else {

            String updatedTitle       = update_title.getText().toString();
            String updatedDescription = update_description.getText().toString();
            Intent intent             = new Intent();
            intent.putExtra("updatedTitle",updatedTitle);
            intent.putExtra("updatedDescription",updatedDescription);
            intent.putExtra("id",id);

            if (selectedImage == null){

                intent.putExtra("image",image);

            }else {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                scaledImage                                 = smallerImage(selectedImage,300);
                scaledImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
                byte[] image                                = byteArrayOutputStream.toByteArray();
                intent.putExtra("image",image);

            }

            setResult(RESULT_OK,intent);
            finish();
        }

    }

    public Bitmap smallerImage (Bitmap img, int maxSize){

        int width   = img.getWidth();
        int height  = img.getHeight();

        float ratio = (float) width / (float) height;

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