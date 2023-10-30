package com.example.videoapp;

import android.content.Context;
import android.os.Environment;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.CacheWriter;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@UnstableApi
public class CacheController {
    private Cache cache;
    private CacheDataSource.Factory cacheDataSourceFactory;
    private CacheDataSource cacheDataSource;

    private ConcurrentHashMap<String, CacheWriter> cacheTask = new ConcurrentHashMap<>();


    File cacheParentDirectory;

    public CacheController(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cacheParentDirectory = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), context.getPackageName());
        } else {
            cacheParentDirectory = new File(context.getFilesDir(), context.getPackageName());
        }
        cache = new SimpleCache(
                new File(cacheParentDirectory, "example_media_cache"),
                new LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024),
                new CacheDatabaseHelper(context));
        cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(new DefaultHttpDataSource.Factory()
                        .setAllowCrossProtocolRedirects(true)
                );
        cacheDataSource = cacheDataSourceFactory.createDataSource();
    }

    private static volatile CacheController cacheController;

    public static void init(Context context) {
        if (cacheController == null) {
            synchronized (CacheController.class) {
                if (cacheController == null) {
                    cacheController = new CacheController(context);
                }
            }
        }
    }

    public static void cacheMedia(ArrayList<String> mediaSources) throws IOException {
        if (cacheController != null) {
            for (String mediaUrl : mediaSources) {
                CacheWriter cacheWriter = new CacheWriter(
                        cacheController.cacheDataSource,
                        new DataSpec.Builder()
                                .setUri(mediaUrl)
                                .setLength((long) (cacheController.getMediaResourceSize(mediaUrl) * 0.1))
                                .build(),
                        null, (requestLength, bytesCached, newBytesCached) -> {
                }
                );
                cacheWriter.cache();
                cacheController.cacheTask.put(mediaUrl, cacheWriter);
            }
        }
    }

    public static void cancelCache(String mediaUrl) {
        if (cacheController != null) {
            CacheWriter cacheWriter = cacheController.cacheTask.get(mediaUrl);
            if (cacheWriter != null) {
                cacheWriter.cancel();
            }
        }
    }


    public static MediaSource.Factory getMediaSourceFactory() {
        CacheController controller = cacheController;
        if (controller != null) {
            return new ProgressiveMediaSource.Factory(controller.cacheDataSourceFactory);
        }
        return null;
    }

    public static void release() {
        CacheController controller = cacheController;
        if (controller != null) {
            for (CacheWriter cacheWriter : controller.cacheTask.values()) {
                cacheWriter.cancel();
            }
            controller.cache.release();
        }
    }

    private long getMediaResourceSize(String mediaUrl) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(mediaUrl).openConnection();
            httpURLConnection.setRequestMethod("HEAD");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String contentLength = httpURLConnection.getHeaderField("Content-Length");
                return Long.parseLong(contentLength);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0L;
    }
}
