package com.example.news.app;

import android.content.Context;

import com.example.news.db.DataSource;
import com.example.news.db.LocalDataSource;
import com.example.news.db.NewsDatabase;
import com.example.news.network.ApiInterface;
import com.example.news.network.RetrofitApiClient;

import retrofit2.Retrofit;

public class Injection {

    private static ApiInterface provideApiInterface() {
        Retrofit retrofitApiClient = RetrofitApiClient.getRetrofitClient();
        return retrofitApiClient.create(ApiInterface.class);
    }

    private static DataSource provideDataSource(Context context) {
        NewsDatabase newsDatabase = NewsDatabase.getInstance(context);
        return new LocalDataSource(newsDatabase.newsDao());
    }

    public static ViewModelProviderFactory provideViewModelProviderFactory(Context context) {
        ApiInterface apiInterface = provideApiInterface();
        DataSource dataSource = provideDataSource(context);
        return new ViewModelProviderFactory(apiInterface, dataSource);
    }
}
