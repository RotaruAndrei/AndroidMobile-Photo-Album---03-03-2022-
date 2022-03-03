package com.example.projectphotoalbum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MyImageViewModel extends AndroidViewModel {

    private MyImagesRepository repository;
    private LiveData<List<MyImages>> imagesList;

    public MyImageViewModel(@NonNull Application application) {
        super(application);

        repository = new MyImagesRepository(application);
        imagesList = repository.getMyImagesList();
    }


    public void insert(MyImages images){
        repository.insert(images);
    }
    public void delete(MyImages images){
        repository.delete(images);
    }
    public void update(MyImages images){
        repository.update(images);
    }

    public LiveData<List<MyImages>> getImagesList (){
        return imagesList;
    }
}
