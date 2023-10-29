package com.example.videoapp;

import org.jetbrains.annotations.TestOnly;
import org.junit.Test;

public class VideoItemTest {
    @Test
    public void testVideoLengthString() {
        VideoItem videoItem = new VideoItem();
        videoItem.setVideoLength(1201);
        System.out.println(videoItem.getStringVideoLength());
    }
}
