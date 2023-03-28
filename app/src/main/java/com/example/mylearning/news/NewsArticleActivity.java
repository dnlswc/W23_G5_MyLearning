package com.example.mylearning.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.login.LoginActivity;
import com.example.mylearning.notepad.NotePageActivity;
import com.example.mylearning.quiz.QuizCatalogueActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class NewsArticleActivity extends AppCompatActivity {
    Article article;

    TextView txtViewArticleTitle, txtViewArticleAuthor, txtViewArticleTime,
            txtViewArticleDescription, txtViewArticleContent, txtViewArticleUrl;
    ImageView imgViewArticleImage;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        getSupportActionBar().setTitle("News Content");

        txtViewArticleTitle = findViewById(R.id.txtViewArticleTitle);
        txtViewArticleAuthor = findViewById(R.id.txtViewArticleAuthor);
        txtViewArticleTime = findViewById(R.id.txtViewArticleTime);
        txtViewArticleDescription = findViewById(R.id.txtViewArticleDescription);
        txtViewArticleContent = findViewById(R.id.txtViewArticleContent);
        txtViewArticleUrl = findViewById(R.id.txtViewArticleUrl);
        imgViewArticleImage = findViewById(R.id.imgViewArticleImage);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myNews);

        article = (Article) getIntent().getSerializableExtra("articleData");

        txtViewArticleTitle.setText(article.getTitle());
        txtViewArticleAuthor.setText(article.getAuthor());
        txtViewArticleTime.setText(article.getPublishedAt());
        txtViewArticleDescription.setText(article.getDescription());
        txtViewArticleContent.setText(article.getContent());
        txtViewArticleUrl.setText(article.getUrl());

        Linkify.addLinks(txtViewArticleUrl, Linkify.WEB_URLS);

        // external library "Picasso" is used here to load image from a URL
        Picasso.get().load(article.getUrlToImage()).into(imgViewArticleImage);

        bottomNavigationView.setOnItemSelectedListener((@NonNull MenuItem item) ->{

            switch (item.getItemId())
            {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myQuiz:
                    startActivity(new Intent(getApplicationContext(), QuizCatalogueActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myNote:
                    startActivity(new Intent(getApplicationContext(), NotePageActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myNews:
                    return true;

                case R.id.myAccount:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }

            return false;
        });
    }
}