package com.noval.multipleimageupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.noval.multipleimageupload.adapter.ListImageAdapter;
import com.noval.multipleimageupload.api.PostMethod;
import com.noval.multipleimageupload.model.ImageModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button imgUpload, imgSelect;
    RecyclerView recyclerView;
    String listBitmap = "";
    ArrayList<ImageModel> imageList;
    ListImageAdapter imageAdapter;
    Context context = MainActivity.this;
    Uri uri;
    private static final int CAMERA_REQUEST = 1888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgSelect = findViewById(R.id.btnSelect);
        imgUpload = findViewById(R.id.btnUpload);
        recyclerView = findViewById(R.id.recyclerview);

        imageList = new ArrayList<>();
        imageAdapter = new ListImageAdapter(context, imageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);

        imgSelect.setOnClickListener(v ->{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }else{
                    requestCamera();
                }
            }else{
                requestCamera();
            }

        });

        imgUpload.setOnClickListener(v->{
            PostMethod.postImageToServer(context, listBitmap, new PostMethod.Post() {
                @Override
                public void onPostSuccess(String message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onPostFailed(String message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void requestCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        uri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(
                        getContentResolver(), uri);
                float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
                int width = 1280;
                int height = Math.round(width / aspectRatio);
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                ImageModel model = new ImageModel();
                model.setImageBitmap(newBitmap);
                model.setUrlGambar("urlGambar");
                imageList.add(model);
                listBitmap += getImageString(newBitmap) + "II0713novalraihan";
                imageAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.e("Image", "2");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestCamera();
            }
            else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    String getImageString(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);

        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}