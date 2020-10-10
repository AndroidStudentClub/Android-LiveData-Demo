package ru.mikhailskiy.livedata;

import android.app.Application;

public class MovieFinderApp extends Application {

    private static MovieFinderApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MovieFinderApp getInstance() {
        return instance;
    }
}



