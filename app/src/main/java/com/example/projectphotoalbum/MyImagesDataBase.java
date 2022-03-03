package com.example.projectphotoalbum;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = MyImages.class,version = 1)
public abstract class MyImagesDataBase extends RoomDatabase {

    private static MyImagesDataBase instance;

    public abstract MyImagesDAO myImagesDAO();

    public static synchronized MyImagesDataBase getInstance(Context context){

        if (instance == null){

            instance = Room.databaseBuilder(context.getApplicationContext(),MyImagesDataBase.class,"my_images_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
