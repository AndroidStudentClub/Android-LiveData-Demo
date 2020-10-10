package ru.mikhailskiy.livedata.data

import android.util.Log
import ru.mikhailskiy.livedata.BuildConfig
import ru.mikhailskiy.livedata.MainActivity
import ru.mikhailskiy.livedata.network.MovieApiClient
import ru.mikhailskiy.livedata.network.MovieApiInterface

class MovieRepository {

    suspend fun fetchNowPlayingMovies(): List<Movie>? {
        val deferredResponse =
            MovieApiClient.apiClient.getNowPlayingMovies(BuildConfig.THE_MOVIE_DATABASE_API, "ru")

        return deferredResponse.results
    }

    suspend fun findMoviesByQuery(queryText: String): List<Movie>? {
        val deferredResponse = MovieApiClient.apiClient.findMovies(BuildConfig.THE_MOVIE_DATABASE_API, queryText)

        return deferredResponse.results
    }
}