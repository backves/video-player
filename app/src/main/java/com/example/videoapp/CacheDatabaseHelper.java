package com.example.videoapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.database.DatabaseProvider;

@UnstableApi
public class CacheDatabaseHelper extends SQLiteOpenHelper implements DatabaseProvider {
    Context context;
    private static final String DATABASE_NAME = "media_cache.db";
    private static final int DATABASE_VERSION = 1;

    public CacheDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
