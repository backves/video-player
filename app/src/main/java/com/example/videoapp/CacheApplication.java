package com.example.videoapp;

import android.app.Application;

import androidx.media3.common.util.UnstableApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;

@UnstableApi
public class CacheApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CacheController.init(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("http://111.229.87.59/norctune%20loop.mp4");
                CacheController.cacheMedia(arrayList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CacheController.release();
    }
}
