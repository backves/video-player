package com.example.videoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.videoapp.databinding.ActivityMainBinding;

import java.util.Objects;

@UnstableApi
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private ActivityMainBinding viewBinding;
    private PlayerView playerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_STORAGE_PERMISSION);

        playerView = viewBinding.player;

//        new NetworkTask().execute();

        playVideo();

//        Log.d("dir", this.getCacheDir() + "/video.mp4");
//        new NetworkTask().execute();

    }

    private void playVideo() {
//        File cacheParentDirectory;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            cacheParentDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getPackageName());
//        } else {
//            cacheParentDirectory = new File(getFilesDir(), getPackageName());
//        }
//        SimpleCache cache = new SimpleCache(new File(cacheParentDirectory, "example_media_cache"), new LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024), new CacheDatabaseHelper(MainActivity.this));
//        CacheDataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory()
//                .setCache(cache)
//                .setUpstreamDataSourceFactory(new DefaultHttpDataSource.Factory()
//                        .setAllowCrossProtocolRedirects(true));

        ExoPlayer player = new ExoPlayer.Builder(MainActivity.this)
                .setMediaSourceFactory(Objects.requireNonNull(CacheController.getMediaSourceFactory()))
//                .setMediaSourceFactory(new ProgressiveMediaSource.Factory((DataSource.Factory) Objects.requireNonNull(cacheDataSourceFactory)))
                .build();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
//                switch (playbackState) {
//                    case Player.STATE_IDLE: {
//                        break;
//                    }
//                    case Player.STATE_BUFFERING: {
//                        break;
//                    }
//                    case Player.STATE_READY: {
//                        break;
//                    }
//                    case Player.STATE_ENDED: {
//
//                    }
//                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
            }
        });

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setPlayWhenReady(true);

        playerView.setPlayer(player);

        player.stop();

        player.addMediaItem(MediaItem.fromUri("http://111.229.87.59/norctune%20loop.mp4"));
        player.addMediaItem(MediaItem.fromUri("https://minigame.vip/Uploads/images/2021/09/18/1631951892_page_img.mp4"));

//        player.addMediaItem(MediaItem.fromUri("http://111.229.87.59/video.mp4"));

        player.prepare();
    }

//    private class NetworkTask extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... params) {
//            ArrayList<String> strings = new ArrayList<>();
//            strings.add("https://minigame.vip/Uploads/images/2021/09/18/1631951892_page_img.mp4");
//            strings.add("http://111.229.87.59/norctune%20loop.mp4");
//            strings.add("http://111.229.87.59/video.mp4");
//            try {
//                CacheController.cacheMedia(strings);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            playVideo();
//        }
//    }
//
//    private class NetworkTask extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... params) {
//            playVideo();
//            OkHttpHelper okHttpHelper = new OkHttpHelper();
//            okHttpHelper.downloadFile("http://111.229.87.59/video.mp4", MainActivity.this.getFilesDir() + "/video.mp4");
//            while (!okHttpHelper.ifFinish()) {
//            }
//            Log.d("Async okhttp", "finish");
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            File videoFile = new File(getFilesDir(), "video.mp4");
//            String videoPath = videoFile.getAbsolutePath();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Objects.requireNonNull(playerView.getPlayer()).play();
        playerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Objects.requireNonNull(playerView.getPlayer()).pause();
        playerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}