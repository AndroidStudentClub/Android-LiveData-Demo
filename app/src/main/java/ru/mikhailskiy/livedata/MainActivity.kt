package ru.mikhailskiy.livedata

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.livedata.adapters.MainCardContainer
import ru.mikhailskiy.livedata.adapters.MovieItem
import ru.mikhailskiy.livedata.data.MovieRepository
import ru.mikhailskiy.livedata.data.MoviesResponse
import ru.mikhailskiy.livedata.network.MovieApiClient
import ru.mikhailskiy.livedata.ui.afterTextChanged

class MainActivity : AppCompatActivity() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewModelFactory = MainViewModelFactory(MovieRepository())
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        search_toolbar.search_edit_text.afterTextChanged { it ->
            mainViewModel.onSearchQuery(it.toString())
        }

        mainViewModel.searchMoviesLiveData.observe(this, Observer { list ->
            val moviesList = list.map { MovieItem(it) }.toList()
            movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }
        })
    }
}

class MainViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}