package com.example.videoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.videoapp.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;

@UnstableApi
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private ActivityMainBinding viewBinding;
    private PlayerView playerView;

    private final String loop = "http://111.229.87.59/norctune%20loop.mp4";
    private final String mini = "https://minigame.vip/Uploads/images/2021/09/18/1631951892_page_img.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_STORAGE_PERMISSION);

        CacheController.init(this);
        cacheVideo(mini);

        playerView = viewBinding.player;
        playVideo();
    }

    private void playVideo() {
        ExoPlayer player = new ExoPlayer.Builder(MainActivity.this)
                .setMediaSourceFactory(Objects.requireNonNull(CacheController.getMediaSourceFactory()))
                .build();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
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


        Log.d("video", "playing");
        player.addMediaItem(MediaItem.fromUri(loop));
        player.addMediaItem(MediaItem.fromUri(mini));
//        player.addMediaItem(MediaItem.fromUri("http://111.229.87.59/video.mp4"));

        player.prepare();
    }

    private void cacheVideo(String url) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(url);
                CacheController.cacheMedia(arrayList);
                Log.d("cache", "cache running");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

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