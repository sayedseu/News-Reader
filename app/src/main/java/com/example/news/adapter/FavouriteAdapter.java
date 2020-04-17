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
import com.example.news.ui.favorite.FavoriteViewModel;
import com.example.news.ui.web.WebViewActivity;
import com.example.news.utils.ImageRoundCorners;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private List<News> newsList;
    private FavoriteViewModel viewModel;
    private Context context;

    public FavouriteAdapter(FavoriteViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.context = context;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favourite_item, viewGroup, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int i) {
        if (newsList != null) {
            News news = newsList.get(i);
            holder.sourceTV.setText(news.getSource());
            holder.titleTV.setText(news.getTitle());
            holder.showImage(news.getImageURL());

            holder.readTV.setOnClickListener(listener -> {
                Intent intent = new Intent(context, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.KEY, news.getSourceURL());
                intent.putExtra(WebViewActivity.KEY, bundle);
                context.startActivity(intent);
            });

            holder.deleteTV.setOnClickListener(listener -> {
                viewModel.delete(news);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (newsList != null) return newsList.size();
        else return 0;
    }

    static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        private TextView sourceTV, titleTV, readTV, deleteTV;
        private ImageView imageView;
        private ProgressBar progressBar;

        FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTV = itemView.findViewById(R.id.source);
            titleTV = itemView.findViewById(R.id.title);
            readTV = itemView.findViewById(R.id.read);
            deleteTV = itemView.findViewById(R.id.delete);
            imageView = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progressBar);
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
