package com.example.videoapp;

public class VideoItem {
    private String name;
    private String format;
    private int videoLength;
    private double size;
    private int length;
    private int width;
    private String resolution;
    private byte[] thumb;

    public VideoItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public byte[] getThumb() {
        return thumb;
    }

    public void setThumb(byte[] thumb) {
        this.thumb = thumb;
    }

    public String getStringVideoLength() {
        int hours = videoLength / 3600;
        int minutes = videoLength % 3600 / 60;
        int seconds = videoLength % 60;
        if (hours != 0) {
            return hours + ":" + addZero(minutes) + ":" + addZero(seconds);
        } else {
            return addZero(minutes) + ":" + addZero(seconds);
        }
    }

    private String addZero(int time) {
        return time > 9 ? String.valueOf(time) : "0" + time;
    }
}
