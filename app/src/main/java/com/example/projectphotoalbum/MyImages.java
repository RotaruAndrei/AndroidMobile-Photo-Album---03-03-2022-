package com.example.projectphotoalbum;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_images")
public class MyImages {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String image_title;
    private String image_description;
    private byte[] image;


    public MyImages(String image_title, String image_description, byte[] image) {
        this.image_title = image_title;
        this.image_description = image_description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getImage_title() {
        return image_title;
    }

    public String getImage_description() {
        return image_description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }
}
