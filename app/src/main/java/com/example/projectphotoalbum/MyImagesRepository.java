package com.example.projectphotoalbum;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImagesRepository {

    private MyImagesDAO myImagesDAO;
    private LiveData<List<MyImages>> myImagesList;

    // executors to run background tasks
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyImagesRepository(Application application) {

        MyImagesDataBase dataBase = MyImagesDataBase.getInstance(application);
        myImagesDAO               = dataBase.myImagesDAO();
        myImagesList              = myImagesDAO.getAllImages();
    }

    public void insert(MyImages images){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.insert(images);
            }
        });
    }

    public void delete(MyImages images){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.delete(images);
            }
        });
    }

    public void update(MyImages images){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.update(images);
            }
        });
    }

    public LiveData<List<MyImages>> getMyImagesList(){
        return myImagesList;
    }


}
