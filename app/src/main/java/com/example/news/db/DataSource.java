package com.example.news.db;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DataSource {
    Single<Long> insert(News news);

    Single<Integer> delete(News news);

    Flowable<List<News>> retrieve();
}
