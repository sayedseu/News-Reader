package com.example.news.view_model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.app.Resource;
import com.example.news.db.DataSource;
import com.example.news.db.News;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InsertViewModel extends ViewModel {
    private static final String TAG = "sayed";
    private final DataSource dataSource;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MediatorLiveData<Resource<Boolean>> insertResult = new MediatorLiveData<>();

    public InsertViewModel(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(News news) {
        insertResult.setValue(Resource.loading(null));
        Single<Long> observable = dataSource.insert(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(Long aLong) {
                Log.i(TAG, "onSuccess: " + aLong);
                insertResult.setValue(Resource.success(true));
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.toString());
                insertResult.setValue(Resource.error("", null));
            }
        });
    }

    public LiveData<Resource<Boolean>> observeInsertResult() {
        return insertResult;
    }

    public void clearObserver() {
        insertResult.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
