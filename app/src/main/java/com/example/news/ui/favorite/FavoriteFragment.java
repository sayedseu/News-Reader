package com.example.news.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapter.FavouriteAdapter;
import com.example.news.app.Injection;
import com.example.news.app.ViewModelProviderFactory;

public class FavoriteFragment extends Fragment {
    private FavoriteViewModel mViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyTV;
    private FavouriteAdapter adapter;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rec);
        progressBar = view.findViewById(R.id.progressBar);
        emptyTV = view.findViewById(R.id.emptyTV);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        mViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(FavoriteViewModel.class);
        adapter = new FavouriteAdapter(mViewModel, requireContext());
        initRecyclerView();
        subscribeObserver();
    }

    private void subscribeObserver() {
        mViewModel.getBookmarks().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        emptyTV.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        emptyTV.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        emptyTV.setVisibility(View.GONE);
                        assert (resource.data == null);
                        if (resource.data.isEmpty()) {
                            emptyTV.setVisibility(View.VISIBLE);
                            adapter.setNewsList(null);
                        }
                        adapter.setNewsList(resource.data);
                        break;
                }
            }
        });

        mViewModel.observeDeletingResult().removeObservers(getViewLifecycleOwner());
        mViewModel.observeDeletingResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case ERROR:
                        showDialog("Unsuccessful", "Deleting unsuccessful.", R.drawable.ic_error);
                        mViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        assert (resource.data != null);
                        if (resource.data) {
                            showDialog("Successful", "Deleting successful.", R.drawable.ic_check_);
                            mViewModel.clearObserver();
                        } else {
                            showDialog("Unsuccessful", "Deleting unsuccessful.", R.drawable.ic_error);
                            mViewModel.clearObserver();
                        }
                        break;
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

    private void showDialog(String title, String message, int iconId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
