package com.example.news.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface NewsDao {
    @Insert
    Single<Long> insert(News news);

    @Delete
    Single<Integer> delete(News news);

    @Query("SELECT * FROM news")
    Flowable<List<News>> getAllNews();
}
