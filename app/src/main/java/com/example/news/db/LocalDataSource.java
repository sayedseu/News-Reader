package com.example.news.db;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalDataSource implements DataSource {
    private final NewsDao newsDao;

    public LocalDataSource(NewsDao newsDao) {
        this.newsDao = newsDao;
    }


    @Override
    public Single<Long> insert(News news) {
        return newsDao.insert(news);
    }

    @Override
    public Single<Integer> delete(News news) {
        return newsDao.delete(news);
    }

    @Override
    public Flowable<List<News>> retrieve() {
        return newsDao.getAllNews();
    }
}
