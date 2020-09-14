package ru.mikhailskiy.livedata;

import android.app.Application;

import retrofit2.Retrofit;
import ru.mikhailskiy.livedata.network.AdvancedApiClient;
import ru.mikhailskiy.livedata.network.MovieApiInterface;

public class MovieFinderApp extends Application {
    public MovieApiInterface service;

    private static MovieFinderApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initRetrofit();
    }

    public static MovieFinderApp getInstance() {
        return instance;
    }

    private void initRetrofit() {

        Retrofit retrofit = AdvancedApiClient.getClient();

        service = retrofit.create(MovieApiInterface.class);
    }
}



