package com.example.mylearning.news;

import android.content.Context;
import android.widget.Toast;

import com.example.mylearning.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getArticles(OnFetchDataListener listener, String query) {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsApiResponse> call = callNewsApi.callArticles("us", "technology", query, context.getString(R.string.news_api_key));

        try {
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Error in fetching news articles:\nReached daily API request limit", Toast.LENGTH_LONG).show();
                    }

                    if (response.body() != null) {
                        listener.onFetchData(response.body().getArticles(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                    listener.onError("Request failed");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getArticlesBySource(OnFetchDataListener listener, String source) {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsApiResponse> call = callNewsApi.callArticlesBySource(source, context.getString(R.string.news_api_key));

        try {
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Error in fetching news articles:\nReached daily API request limit", Toast.LENGTH_LONG).show();
                    }

                    if (response.body() != null) {
                        listener.onFetchData(response.body().getArticles(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                    listener.onError("Request failed");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public interface CallNewsApi {
        @GET("top-headlines")
        Call<NewsApiResponse> callArticles(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String apiKey
        );

        @GET("top-headlines")
        Call<NewsApiResponse> callArticlesBySource(
                @Query("sources") String source,
                @Query("apiKey") String apiKey
        );
    }
}
