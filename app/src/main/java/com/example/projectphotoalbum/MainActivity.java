package com.example.projectphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private MyImageViewModel viewModel;
    private MyImagesAdapter adapter;
    private ActivityResultLauncher<Intent> activityResultLauncherforAddImage;
    private ActivityResultLauncher<Intent> activityResultLauncherforUpdateImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate: checks");

        registerActivityForAddImage();
        registerActivityForUpdateImage();

        recyclerView         = findViewById(R.id.recycleViewId);
        floatingActionButton = findViewById(R.id.floating_btn);
        adapter              = new MyImagesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MyImageViewModel.class);



        viewModel.getImagesList().observe(MainActivity.this, new Observer<List<MyImages>>() {
            @Override
            public void onChanged(List<MyImages> myImages) {

                adapter.setImagesList(myImages);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,AddImageActivity.class);
                activityResultLauncherforAddImage.launch(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                viewModel.delete(adapter.getCurrentImage(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setListener(listener -> {

            Intent intent = new Intent(MainActivity.this,UpdateImageActivity.class);
            intent.putExtra("id",listener.getId());
            intent.putExtra("title",listener.getImage_title());
            intent.putExtra("description",listener.getImage_description());
            intent.putExtra("image",listener.getImage());
            activityResultLauncherforUpdateImage.launch(intent);
        });
    }

    public void registerActivityForAddImage(){
        activityResultLauncherforAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            int resultCode = result.getResultCode();
            Intent data    = result.getData();

            if (resultCode == RESULT_OK && data != null){

                String title       = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                byte[] image       = data.getByteArrayExtra("image");

                MyImages saveImage = new MyImages(title,description,image);
                viewModel.insert(saveImage);
            }
        });
    }

    public void registerActivityForUpdateImage(){
        activityResultLauncherforUpdateImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {

            // check first if the user updated anything or is just returning back to the main activity
            // if the user made changes then we save it to the database
            int resultCode         = result.getResultCode();
            Intent data            = result.getData();
            if (resultCode == RESULT_OK && data != null){
                String title       = data.getStringExtra("updatedTitle");
                String description = data.getStringExtra("updatedDescription");
                byte[] image       = data.getByteArrayExtra("image");
                int id             = data.getIntExtra("id",-1);
                MyImages myImage   = new MyImages(title,description,image);
                myImage.setId(id);

                viewModel.update(myImage);
            }
        });
    }
}