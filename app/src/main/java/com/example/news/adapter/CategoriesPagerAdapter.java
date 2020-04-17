package com.example.news.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.news.ui.categories.BusinessFragment;
import com.example.news.ui.categories.EntertainmentFragment;
import com.example.news.ui.categories.GeneralFragment;
import com.example.news.ui.categories.HealthFragment;
import com.example.news.ui.categories.ScienceFragment;
import com.example.news.ui.categories.SportsFragment;
import com.example.news.ui.categories.TechnologyFragment;

public class CategoriesPagerAdapter extends FragmentPagerAdapter {


    public CategoriesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return BusinessFragment.newInstance();
            case 1:
                return EntertainmentFragment.newInstance();
            case 2:
                return GeneralFragment.newInstance();
            case 3:
                return HealthFragment.newInstance();
            case 4:
                return ScienceFragment.newInstance();
            case 5:
                return SportsFragment.newInstance();
            case 6:
                return TechnologyFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Business";
            case 1:
                return "Entertainment";
            case 2:
                return "General";
            case 3:
                return "Health";
            case 4:
                return "Science";
            case 5:
                return "Sports";
            case 6:
                return "Technology";
        }
        return null;
    }
}
