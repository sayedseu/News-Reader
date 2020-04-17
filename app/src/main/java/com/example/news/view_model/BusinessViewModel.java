package com.example.news.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.app.Resource;
import com.example.news.model.news.ServerResponse;
import com.example.news.network.ApiInterface;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BusinessViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<ServerResponse>> news;

    public BusinessViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<ServerResponse>> getNews() {
        if (news == null) {
            news = new MediatorLiveData<>();
            news.setValue(Resource.loading(null));
            LiveData<Resource<ServerResponse>> source = LiveDataReactiveStreams.fromPublisher(
                    apiInterface.getBusiness()
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
