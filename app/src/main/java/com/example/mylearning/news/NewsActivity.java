package com.example.mylearning.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.notepad.NotePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class NewsActivity extends AppCompatActivity implements ArticleOnClickListener, View.OnClickListener {
    ProgressDialog dialog;

    Button btnAll, btnArsTechnica, btnEngadget,
            btnTechCrunch, btnTechRadar, btnTheNextWeb,
            btnTheVerge, btnVox, btnWired;
    SearchView searchViewQuery;

    RecyclerView recyclerViewNews;
    NewsRecyclerViewAdapter adapter;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        btnAll = findViewById(R.id.btnAll);
        btnArsTechnica = findViewById(R.id.btnArsTechnica);
        btnEngadget = findViewById(R.id.btnEngadget);
        btnTechCrunch = findViewById(R.id.btnTechCrunch);
        btnTechRadar = findViewById(R.id.btnTechRadar);
        btnTheNextWeb = findViewById(R.id.btnTheNextWeb);
        btnTheVerge = findViewById(R.id.btnTheVerge);
        btnVox = findViewById(R.id.btnVox);
        btnWired = findViewById(R.id.btnWired);

        btnArsTechnica.setOnClickListener(this);
        btnEngadget.setOnClickListener(this);
        btnTechCrunch.setOnClickListener(this);
        btnTechRadar.setOnClickListener(this);
        btnTheNextWeb.setOnClickListener(this);
        btnTheVerge.setOnClickListener(this);
        btnVox.setOnClickListener(this);
        btnWired.setOnClickListener(this);

        searchViewQuery = findViewById(R.id.searchViewQuery);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myNews);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading news articles...");
        dialog.show();

        RequestManager requestManager = new RequestManager(this);
        requestManager.getArticles(listener, null);

        btnAll.setOnClickListener((View v) -> {
            dialog.setTitle("Loading news articles...");
            dialog.show();

            requestManager.getArticles(listener, null);
        });

        searchViewQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Searching for news articles about " + query + "...");
                dialog.show();

                RequestManager requestManager = new RequestManager(NewsActivity.this);
                requestManager.getArticles(listener, query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        bottomNavigationView.setOnItemSelectedListener((@NonNull MenuItem item) ->{

            switch (item.getItemId())
            {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myQuiz:
                    return true;

                case R.id.myNote:
                    startActivity(new Intent(getApplicationContext(), NotePageActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myNews:
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }

            return false;
        });
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<Article> articleList, String message) {
            if (articleList.isEmpty()) {
                Toast.makeText(NewsActivity.this, "No news articles available", Toast.LENGTH_SHORT).show();
            } else {
                showArticles(articleList);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(NewsActivity.this, "Unable to load news articles", Toast.LENGTH_SHORT).show();
        }
    };

    private void showArticles(List<Article> articleList) {
        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setHasFixedSize(true);

        GridLayoutManager gm = new GridLayoutManager(this, 1);
        recyclerViewNews.setLayoutManager(gm);

        adapter = new NewsRecyclerViewAdapter(articleList, this);
        recyclerViewNews.setAdapter(adapter);
    }

    @Override
    public void onArticleClicked(Article article) {
        Intent newsIntent = new Intent(NewsActivity.this, NewsArticleActivity.class);
        newsIntent.putExtra("articleData", article);
        startActivity(newsIntent);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;

        String website = button.getText().toString();
        String source;

        if (website.equalsIgnoreCase("Vox")) {
            source = "recode";
        } else {
            source = website.toLowerCase().replace(' ', '-');
        }

        dialog.setTitle("Loading news articles from " + website + "...");
        dialog.show();

        RequestManager requestManager = new RequestManager(this);
        requestManager.getArticlesBySource(listener, source);
    }
}