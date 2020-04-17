package com.example.news.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.model.source.Source;
import com.example.news.ui.web.WebViewActivity;

import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.GridViewHolder> {
    private List<Source> sourceList;
    private Context context;

    public SourceAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Source> sourceList) {
        this.sourceList = sourceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_source, viewGroup, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int i) {
        if (sourceList != null) {
            holder.title.setText(sourceList.get(i).getName());
            holder.cardView.setOnClickListener(listener -> {
                Intent intent = new Intent(context, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.KEY, sourceList.get(i).getUrl());
                intent.putExtra(WebViewActivity.KEY, bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (sourceList != null) return sourceList.size();
        else return 0;
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private CardView cardView;

        private GridViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sourceTV);
            cardView = itemView.findViewById(R.id.parents);
        }
    }
}
