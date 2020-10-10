package ru.mikhailskiy.livedata.network


import retrofit2.http.GET
import retrofit2.http.Query
import ru.mikhailskiy.livedata.data.MoviesResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): MoviesResponse

    @GET("search/movie")
    suspend fun findMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MoviesResponse
}
