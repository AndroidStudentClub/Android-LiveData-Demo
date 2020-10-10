package ru.mikhailskiy.livedata.data

import io.reactivex.Single
import ru.mikhailskiy.livedata.BuildConfig
import ru.mikhailskiy.livedata.network.MovieApiClient

class MovieRepository {

    fun fetchNowPlayingMovies(): Single<List<Movie>> {
        val deferredResponse =
            MovieApiClient.apiClient.getNowPlayingMovies(BuildConfig.THE_MOVIE_DATABASE_API, "ru")

        return deferredResponse.map { it.results }
    }

    fun findMoviesByQuery(queryText: String): Single<List<Movie>> {
        val deferredResponse =
            MovieApiClient.apiClient.findMovies(BuildConfig.THE_MOVIE_DATABASE_API, queryText)

        return deferredResponse.map { it.results }
    }
}