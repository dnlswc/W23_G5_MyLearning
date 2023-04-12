package com.example.mylearning.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearning.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {
    private List<Article> articleList;
    private ArticleOnClickListener listener;

    public NewsRecyclerViewAdapter() {
    }

    public NewsRecyclerViewAdapter(List<Article> articleList, ArticleOnClickListener listener) {
        this.articleList = articleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_news_item, parent, false);

        NewsViewHolder holder = new NewsViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        String sourceName = articleList.get(position).getSource().getName();

        holder.txtViewNewsTitle.setText(articleList.get(position).getTitle());

        if (sourceName.equalsIgnoreCase("Recode")) {
            holder.txtViewNewsSource.setText("Vox");
        } else {
            holder.txtViewNewsSource.setText(sourceName);
        }

        if (articleList.get(position).getUrlToImage() != null) {
            // external library "Picasso" is used here to load image from a URL
            try {
                Picasso.get()
                        .load(articleList.get(position).getUrlToImage())
                        .resize(1920, 1200)
                        .onlyScaleDown()
                        .into(holder.imgViewNewsImage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        holder.cardViewNewsContainer.setOnClickListener((View v) -> {
            listener.onArticleClicked(articleList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewNewsContainer;
        TextView txtViewNewsTitle, txtViewNewsSource;
        ImageView imgViewNewsImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewNewsContainer = itemView.findViewById(R.id.cardViewNewsContainer);
            txtViewNewsTitle = itemView.findViewById(R.id.txtViewNewsTitle);
            txtViewNewsSource = itemView.findViewById(R.id.txtViewNewsSource);
            imgViewNewsImage = itemView.findViewById(R.id.imgViewNewsImage);
        }
    }
}
