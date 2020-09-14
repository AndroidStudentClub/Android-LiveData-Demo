package ru.mikhailskiy.livedata.network


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mikhailskiy.livedata.data.MoviesResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("movie/popular")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

}
