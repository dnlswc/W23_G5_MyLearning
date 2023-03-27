package com.example.mylearning.news;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {
    void onFetchData(List<Article> articleList, String message);
    void onError(String message);
}
