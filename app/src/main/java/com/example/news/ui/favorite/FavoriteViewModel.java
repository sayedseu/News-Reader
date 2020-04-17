package com.example.news.ui.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.app.Resource;
import com.example.news.db.DataSource;
import com.example.news.db.News;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class FavoriteViewModel extends ViewModel {
    private final DataSource dataSource;
    private MutableLiveData<Resource<List<News>>> bookMarksNews = new MutableLiveData<>();
    private MutableLiveData<Resource<Boolean>> deletingResult = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public FavoriteViewModel(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LiveData<Resource<List<News>>> getBookmarks() {
        bookMarksNews.setValue(Resource.loading(null));
        disposable.add(dataSource.retrieve()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<List<News>>() {
                    @Override
                    public void onNext(List<News> news) {
                        bookMarksNews.setValue(Resource.success(news));
                    }

                    @Override
                    public void onError(Throwable t) {
                        bookMarksNews.setValue(Resource.error("", null));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
        return bookMarksNews;
    }

    public void delete(News news) {
        deletingResult.setValue(Resource.loading(null));
        Single<Integer> integerSingle = dataSource.delete(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        integerSingle.subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(Integer integer) {
                if (integer == 1) deletingResult.setValue(Resource.success(true));
                else deletingResult.setValue(Resource.success(false));
            }

            @Override
            public void onError(Throwable e) {
                deletingResult.setValue(Resource.error("", null));
            }
        });
    }

    public LiveData<Resource<Boolean>> observeDeletingResult() {
        return deletingResult;
    }

    public void clearObserver() {
        deletingResult.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
