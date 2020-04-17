package com.example.news.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {News.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {
    private static volatile NewsDatabase instance;

    public static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (NewsDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext()
                            , NewsDatabase.class, "news_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract NewsDao newsDao();
}
