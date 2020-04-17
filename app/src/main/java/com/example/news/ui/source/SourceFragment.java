package com.example.news.ui.source;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapter.SourceAdapter;
import com.example.news.app.Injection;
import com.example.news.app.ViewModelProviderFactory;

public class SourceFragment extends Fragment {
    private static final String TAG = "sayed";
    private SourceViewModel mViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyTV;
    private SourceAdapter adapter;

    public static SourceFragment newInstance() {
        return new SourceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.source_fragment, container, false);
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
        mViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(SourceViewModel.class);
        adapter = new SourceAdapter(requireContext());
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
                            Log.i(TAG, "subscribeObserver: ");
                            adapter.setData(resource.data.getSources());
                        }
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

}
