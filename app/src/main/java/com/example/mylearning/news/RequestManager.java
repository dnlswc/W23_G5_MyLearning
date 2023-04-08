package com.example.mylearning.news;

import android.content.Context;
import android.util.MutableBoolean;

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
    String[] apiKeys;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getArticles(OnFetchDataListener listener, String query) {
        apiKeys = context.getResources().getStringArray(R.array.news_api_key);
        final MutableBoolean responseIsSuccessful = new MutableBoolean(false);
        int i = 0;

        NewsApiService service = retrofit.create(NewsApiService.class);

        do {
            Call<NewsApiResponse> call = service.getArticles("us", "technology", query, apiKeys[i]);

            try {
                call.enqueue(new Callback<NewsApiResponse>() {
                    @Override
                    public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                        if (response.isSuccessful()) {
                            responseIsSuccessful.value = true;
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

            i++;
        } while (!responseIsSuccessful.value && i < apiKeys.length);
    }

    public void getArticlesBySource(OnFetchDataListener listener, String source) {
        apiKeys = context.getResources().getStringArray(R.array.news_api_key);
        final MutableBoolean responseIsSuccessful = new MutableBoolean(false);
        int i = 0;

        NewsApiService service = retrofit.create(NewsApiService.class);

        do {
            Call<NewsApiResponse> call = service.getArticlesBySource(source, apiKeys[i]);

            try {
                call.enqueue(new Callback<NewsApiResponse>() {
                    @Override
                    public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                        if (response.isSuccessful()) {
                            responseIsSuccessful.value = true;
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

            i++;
        } while (!responseIsSuccessful.value && i < apiKeys.length);
    }

    public interface NewsApiService {
        @GET("top-headlines")
        Call<NewsApiResponse> getArticles(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String apiKey
        );

        @GET("top-headlines")
        Call<NewsApiResponse> getArticlesBySource(
                @Query("sources") String source,
                @Query("apiKey") String apiKey
        );
    }
}
