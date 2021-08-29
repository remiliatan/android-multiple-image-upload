package com.noval.multipleimageupload.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class PostMethod {
    public interface Post{
        void onPostSuccess(String message);
        void onPostFailed(String message);
    }

    public static void postImageToServer(Context context, String imageData, Post postMethod){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.urlServer + "/upload.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int success = jsonObject.getInt("success");
                String message = jsonObject.getString("message");
                if(success == 1){
                    postMethod.onPostSuccess(message);
                }else{
                    postMethod.onPostFailed(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error Response", error.getMessage())){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("dataImage", imageData);
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
