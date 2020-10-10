package ru.mikhailskiy.livedata.network


import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.coroutines.Deferred
import retrofit2.Response
import ru.mikhailskiy.livedata.data.MoviesResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<MoviesResponse>

    @GET("search/movie")
    fun findMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Single<MoviesResponse>

    @GET("movie/popular")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Deferred<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Deferred<MoviesResponse>

}
