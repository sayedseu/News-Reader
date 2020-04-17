package com.example.news.network;

import com.example.news.model.news.ServerResponse;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("/v2/top-headlines?country=us&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getHeadlines();

    @GET("/v2/top-headlines?category=entertainment&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getEntertainments();

    @GET("/v2/top-headlines?category=general&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getGenerals();

    @GET("/v2/top-headlines?category=business&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getBusiness();

    @GET("/v2/top-headlines?category=health&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getHealths();

    @GET("/v2/top-headlines?category=science&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getSciencs();

    @GET("/v2/top-headlines?category=sports&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getSports();

    @GET("/v2/top-headlines?category=technology&apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<ServerResponse> getTechnologys();

    @GET("/v2/everything")
    Flowable<ServerResponse> getResultBySearching(@QueryMap Map<String, String> filters);

    @GET("/v2/sources?apiKey=ff084860c50a4968948d1861ad035b81")
    Flowable<com.example.news.model.source.ServerResponse> getSource();
}
