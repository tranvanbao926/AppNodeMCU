package com.example.baotran.appnodemcu;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Connect {

    public static String getData(String urlUser){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                    .url(urlUser)
                    .build();
//Creating request objects for make network calls
        try {
            Response response = client.newCall(request).execute(); // Đọc đường dẫn, Sending and receiving network calls
            return response.body().string();
        } catch (IOException error) {
            return null;
        }
    }
}
