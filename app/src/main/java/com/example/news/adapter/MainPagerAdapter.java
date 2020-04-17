package com.example.news.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.news.ui.categories.CategoriesFragment;
import com.example.news.ui.favorite.FavoriteFragment;
import com.example.news.ui.headline.HeadlineFragment;
import com.example.news.ui.source.SourceFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return HeadlineFragment.newInstance();
            case 1:
                return CategoriesFragment.newInstance();
            case 2:
                return SourceFragment.newInstance();
            case 3:
                return FavoriteFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
