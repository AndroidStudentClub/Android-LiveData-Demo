package ru.mikhailskiy.livedata

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.livedata.adapters.MainCardContainer
import ru.mikhailskiy.livedata.adapters.MovieItem
import ru.mikhailskiy.livedata.data.MoviesResponse
import ru.mikhailskiy.livedata.network.MovieApiClient
import ru.mikhailskiy.livedata.ui.afterTextChanged

class MainActivity : AppCompatActivity() {

    val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_toolbar.search_edit_text.afterTextChanged { it ->
            Log.d(TAG, it.toString())
        }

        val nowPlaying = MovieApiClient.apiClient.getNowPlayingMovies(API_KEY, "ru")

        nowPlaying.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>, response: Response<MoviesResponse>
            ) {
                val movies = response.body()!!.results
                // Передаем результат в adapter и отображаем элементы
                val moviesList = listOf(
                    MainCardContainer(
                        R.string.recommended,
                        {},
                        movies.map { MovieItem(it) }.toList()
                    )
                )

                movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                // Логируем ошибку
                Log.e(TAG, t.toString())
            }
        })

        val getTopRated = MovieApiClient.apiClient.getTopRatedMovies(API_KEY, "ru")

        getTopRated.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>, response: Response<MoviesResponse>
            ) {
                val movies = response.body()!!.results
                // Передаем результат в adapter и отображаем элементы
                val moviesList = listOf(
                    MainCardContainer(R.string.popular, {}, movies.map { MovieItem(it) }.toList())
                )
                adapter.apply { addAll(moviesList) }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                // Логируем ошибку
                Log.e(TAG, t.toString())
            }
        })

        val getUpcoming = MovieApiClient.apiClient.getUpcomingMovies(API_KEY, "ru")

        getUpcoming.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>, response: Response<MoviesResponse>
            ) {
                val movies = response.body()!!.results
                // Передаем результат в adapter и отображаем элементы
                val moviesList = listOf(
                    MainCardContainer(R.string.upcoming, {}, movies.map { MovieItem(it) }.toList())
                )

                adapter.apply { addAll(moviesList) }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                // Логируем ошибку
                Log.e(TAG, t.toString())
            }
        })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}

