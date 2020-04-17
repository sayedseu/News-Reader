package com.example.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.db.News;
import com.example.news.model.news.Article;
import com.example.news.ui.web.WebViewActivity;
import com.example.news.utils.ImageRoundCorners;
import com.example.news.view_model.InsertViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder> {
    private List<Article> articleList;
    private Context context;
    private InsertViewModel viewModel;

    public ResponseAdapter(Context context, InsertViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setData(List<Article> articleList) {
        this.articleList = articleList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_news_item, parent, false);
        return new ResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        if (articleList != null) {
            Article article = articleList.get(position);
            holder.titleTV.setText(article.getTitle());
            holder.timeTV.setText(article.getPublishedAt());
            holder.sourceTV.setText(article.getSource().getName());
            holder.showImage(article.getUrlToImage());

            holder.readTV.setOnClickListener(listen -> {
                Intent intent = new Intent(context, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.KEY, article.getUrl());
                intent.putExtra(WebViewActivity.KEY, bundle);
                context.startActivity(intent);
            });

            holder.bookmarksTV.setOnClickListener(listener -> {
                News news = new News(article.getTitle(), article.getSource().getName(), article.getUrl(), article.getUrlToImage());
                viewModel.insert(news);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (articleList != null) return articleList.size();
        else return 0;
    }

    static class ResponseViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV, sourceTV, timeTV;
        private ImageView imageView;
        private TextView readTV, bookmarksTV;
        private ProgressBar progressBar;

        ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.time);
            titleTV = itemView.findViewById(R.id.title);
            sourceTV = itemView.findViewById(R.id.source);
            imageView = itemView.findViewById(R.id.newsIV);
            readTV = itemView.findViewById(R.id.read);
            bookmarksTV = itemView.findViewById(R.id.bookmarks);
            progressBar = itemView.findViewById(R.id.newsPB);
        }

        void showImage(String url) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(url)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .transform(new ImageRoundCorners())
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }
}
