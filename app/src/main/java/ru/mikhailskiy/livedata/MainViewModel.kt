package ru.mikhailskiy.livedata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mikhailskiy.livedata.data.Movie
import ru.mikhailskiy.livedata.data.MovieRepository

class MainViewModel(private val repository: MovieRepository) : ViewModel() {

    private var debouncePeriod: Long = 500

    private val compositeDisposable = CompositeDisposable()

    val searchMoviesLiveData = MutableLiveData<List<Movie>>()

    fun onFragmentReady() {
        //TODO Fetch Popular Movies
    }

    fun onSearchQuery(query: String) {
        if (query.length > 2) {
            fetchMovieByQuery(query)
        }
    }


    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            compositeDisposable.add(
                repository.fetchNowPlayingMovies()
                    .subscribe({ list ->
                        searchMoviesLiveData.postValue(list)
                    }, {
                        Log.e("Error", it.toString())
                    })
            )

        }
    }

    private fun fetchMovieByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val movies = repository.findMoviesByQuery(query)
            compositeDisposable.add(
                movies.subscribe { it ->
                    searchMoviesLiveData.postValue(it)
                }
            )
        }
    }

    fun onMovieClicked(movie: Movie) {
        // TODO handle navigation to details screen event
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}