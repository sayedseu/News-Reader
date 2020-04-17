package com.example.news.ui.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.app.Resource;
import com.example.news.model.source.ServerResponse;
import com.example.news.network.ApiInterface;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SourceViewModel extends ViewModel {
    private static final String NOT_STATUS = "not_status";
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<ServerResponse>> news;

    public SourceViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<ServerResponse>> getNews() {
        if (news == null) {
            news = new MediatorLiveData<>();
            news.setValue(Resource.loading(null));
            LiveData<Resource<ServerResponse>> source = LiveDataReactiveStreams.fromPublisher(
                    apiInterface.getSource()
                            .onErrorReturn(throwable -> {
                                ServerResponse errorResponse = new ServerResponse();
                                errorResponse.setStatus(NOT_STATUS);
                                return errorResponse;
                            })
                            .map((Function<ServerResponse, Resource<ServerResponse>>) serverResponse -> {
                                if (!serverResponse.getStatus().equals(NOT_STATUS)) {
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
