package ru.mikhailskiy.livedata.data

import ru.mikhailskiy.livedata.BuildConfig
import ru.mikhailskiy.livedata.network.MovieApiClient

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