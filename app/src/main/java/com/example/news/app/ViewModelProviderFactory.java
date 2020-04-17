package com.example.news.app;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.news.db.DataSource;
import com.example.news.network.ApiInterface;
import com.example.news.ui.favorite.FavoriteViewModel;
import com.example.news.ui.headline.HeadlineViewModel;
import com.example.news.ui.search.SearchViewModel;
import com.example.news.ui.source.SourceViewModel;
import com.example.news.view_model.BusinessViewModel;
import com.example.news.view_model.EntertainmentViewModel;
import com.example.news.view_model.GeneralViewModel;
import com.example.news.view_model.HealthViewModel;
import com.example.news.view_model.InsertViewModel;
import com.example.news.view_model.ScienceViewModel;
import com.example.news.view_model.SportsViewModel;
import com.example.news.view_model.TechnologyViewModel;

public class ViewModelProviderFactory implements androidx.lifecycle.ViewModelProvider.Factory {
    private final ApiInterface apiInterface;
    private final DataSource dataSource;

    public ViewModelProviderFactory(ApiInterface apiInterface, DataSource dataSource) {
        this.apiInterface = apiInterface;
        this.dataSource = dataSource;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HeadlineViewModel.class)) {
            return (T) new HeadlineViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(BusinessViewModel.class)) {
            return (T) new BusinessViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(EntertainmentViewModel.class)) {
            return (T) new EntertainmentViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(GeneralViewModel.class)) {
            return (T) new GeneralViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(HealthViewModel.class)) {
            return (T) new HealthViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(ScienceViewModel.class)) {
            return (T) new ScienceViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(SportsViewModel.class)) {
            return (T) new SportsViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(TechnologyViewModel.class)) {
            return (T) new TechnologyViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(SourceViewModel.class)) {
            return (T) new SourceViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(InsertViewModel.class)) {
            return (T) new InsertViewModel(dataSource);
        }
        if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
            return (T) new FavoriteViewModel(dataSource);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
