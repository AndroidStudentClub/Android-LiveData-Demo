package ru.mikhailskiy.livedata.data.network


import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mikhailskiy.livedata.BuildConfig
import ru.mikhailskiy.livedata.data.MoviesResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
   fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru"
    ): Single<MoviesResponse>

    @GET("search/movie")
    suspend fun findMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MoviesResponse
}
