package com.noval.multipleimageupload.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.noval.multipleimageupload.R;
import com.noval.multipleimageupload.model.ImageModel;

import java.util.ArrayList;

public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.ViewHolder> {

    private final ArrayList<ImageModel> mData;
    private final LayoutInflater mInflater;
    private final Context context;

    public ListImageAdapter(Context context, ArrayList<ImageModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_data, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap bitmapImage = mData.get(position).getImageBitmap();
        Glide.with(context).load(bitmapImage).into(holder.myImage);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView myImage;

        ViewHolder(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.imageData);
        }
    }
}
