package com.example.news.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.app.Resource;
import com.example.news.model.news.ServerResponse;
import com.example.news.network.ApiInterface;
import com.facebook.common.internal.ImmutableMap;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<ServerResponse>> news;

    public SearchViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<ServerResponse>> getNews(String query) {
        if (news == null) {

            String q = "q";
            String k = "apiKey";
            String key = "ff084860c50a4968948d1861ad035b81";

            news = new MediatorLiveData<>();
            news.setValue(Resource.loading(null));
            LiveData<Resource<ServerResponse>> source = LiveDataReactiveStreams.fromPublisher(
                    apiInterface.getResultBySearching(ImmutableMap.of(k, key, q, "('" + query + "')"))
                            .onErrorReturn(throwable -> {
                                ServerResponse errorResponse = new ServerResponse();
                                errorResponse.setTotalResults(-1);
                                return errorResponse;
                            })
                            .map((Function<ServerResponse, Resource<ServerResponse>>) serverResponse -> {
                                if (serverResponse.getTotalResults() > 0) {
                                    return Resource.success(serverResponse);
                                } else return Resource.error("", null);
                            }).subscribeOn(Schedulers.io()));
            news.addSource(source, serverResponseResource -> {
                news.setValue(serverResponseResource);
                news.removeSource(source);
            });
        }
        return news;
    }
}
