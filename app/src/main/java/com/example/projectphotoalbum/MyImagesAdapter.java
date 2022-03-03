package com.example.projectphotoalbum;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.ViewHolder> {

    private List<MyImages> imagesList = new ArrayList<>();
    private OnImageClickListener listener;
    public void setImagesList(List<MyImages> imagesList) {
        this.imagesList       = imagesList;
        notifyDataSetChanged();
    }

    public MyImages getCurrentImage(int position){
        return imagesList.get(position);
    }

    public interface OnImageClickListener{

        void onImageClicked(MyImages image);
    }

    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        MyImages img = imagesList.get(position);
//        holder.title.setText(img.getImage_title());
//        holder.description.setText(img.getImage_description());
//        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(img.getImage(),0,img.getImage().length));

        holder.title.setText(imagesList.get(position).getImage_title());
        holder.description.setText(imagesList.get(position).getImage_description());
        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(imagesList.get(position).getImage(),0,imagesList.get(position).getImage().length));

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img         = itemView.findViewById(R.id.card_img);
            title       = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.card_description);

            itemView.setOnClickListener(view -> {

                int position = getAdapterPosition();

                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onImageClicked(imagesList.get(position));
                }
            });
        }
    }
}
