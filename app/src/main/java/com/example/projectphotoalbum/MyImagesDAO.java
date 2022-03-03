package com.example.projectphotoalbum;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyImagesDAO {

    @Insert
    void insert(MyImages img);

    @Delete
    void delete(MyImages img);

    @Update
    void update(MyImages img);

    @Query("SELECT * FROM my_images ORDER BY id ASC")
    LiveData<List<MyImages>> getAllImages();
}
