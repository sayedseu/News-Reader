package com.example.news.ui.headline;

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
import com.example.news.adapter.ResponseAdapter;
import com.example.news.app.Injection;
import com.example.news.app.ViewModelProviderFactory;
import com.example.news.view_model.InsertViewModel;

public class HeadlineFragment extends Fragment {
    private InsertViewModel insertViewModel;
    private HeadlineViewModel mViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyTV;
    private ResponseAdapter adapter;

    public static HeadlineFragment newInstance() {
        return new HeadlineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.headline_fragment, container, false);
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
        mViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(HeadlineViewModel.class);
        insertViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(InsertViewModel.class);
        adapter = new ResponseAdapter(requireContext(), insertViewModel);
        initRecyclerView();
        subscribeObserver();
    }

    private void subscribeObserver() {
        mViewModel.getNews().observe(getViewLifecycleOwner(), resource -> {
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
                        if (resource.data != null) {
                            adapter.setData(resource.data.getArticles());
                        }
                }
            }
        });

        insertViewModel.observeInsertResult().removeObservers(getViewLifecycleOwner());
        insertViewModel.observeInsertResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case ERROR:
                        showDialog("Unsuccessful", "Saved unsuccessful.", R.drawable.ic_error);
                        insertViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        if (resource.data) {
                            showDialog("Successful", "Saved successful.", R.drawable.ic_check_);
                            insertViewModel.clearObserver();
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
