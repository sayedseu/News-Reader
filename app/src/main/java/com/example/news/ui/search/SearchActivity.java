package com.example.news.ui.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapter.ResponseAdapter;
import com.example.news.app.Injection;
import com.example.news.app.ViewModelProviderFactory;
import com.example.news.view_model.InsertViewModel;

public class SearchActivity extends AppCompatActivity {
    private SearchViewModel viewModel;
    private InsertViewModel insertViewModel;
    private TextView nothingTV;
    private ProgressBar progressBar;
    private ResponseAdapter adapter;
    private String query = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String toolText = null;
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String s = intent.getStringExtra(SearchManager.QUERY);
            toolText = s;
            String[] arr = s.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                stringBuilder.append(arr[i]);
                if (i < arr.length - 1) {
                    stringBuilder.append("+");
                }
            }
            query = stringBuilder.toString();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(toolText);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(this.getApplication());
        viewModel = new ViewModelProvider(this, providerFactory).get(SearchViewModel.class);
        insertViewModel = new ViewModelProvider(this, providerFactory).get(InsertViewModel.class);
        adapter = new ResponseAdapter(this, insertViewModel);
        nothingTV = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        initRecyclerView();
        subscribeObserver();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

    private void subscribeObserver() {
        assert (query != null);
        viewModel.getNews(query).observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        nothingTV.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        nothingTV.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.data != null) {
                            nothingTV.setVisibility(View.GONE);
                            adapter.setData(resource.data.getArticles());
                        } else nothingTV.setVisibility(View.VISIBLE);
                }
            }
        });

        insertViewModel.observeInsertResult().removeObservers(this);
        insertViewModel.observeInsertResult().observe(this, resource -> {
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

    private void showDialog(String title, String message, int iconId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
