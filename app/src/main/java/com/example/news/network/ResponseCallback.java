package com.example.news.network;

public interface ResponseCallback<T> {

    void onSucess(T data);

    void onError(Throwable throwable);
}
