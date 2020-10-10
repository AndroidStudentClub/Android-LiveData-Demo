package ru.mikhailskiy.livedata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mikhailskiy.livedata.data.Movie
import ru.mikhailskiy.livedata.data.MovieRepository

class MainViewModel(private val repository: MovieRepository) : ViewModel() {

    private var debouncePeriod: Long = 500
    private var searchJob: Job? = null

    // 1. Создаём MutableLiveData для передачи данных в View
    val searchMoviesLiveData = MutableLiveData<List<Movie>>()

    fun onFragmentReady() {
        //TODO Fetch Popular Movies
    }

    // 2. Вызывается из View для передачи строки поиска в сетевой запрос
    fun onSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            if (query.length > 2) {
                fetchMovieByQuery(query)
            }
        }
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = repository.fetchNowPlayingMovies()
            searchMoviesLiveData.postValue(movies)
        }
    }

    // 3. Используя query обращаемся к репозиторию для поиска фильмов
    private fun fetchMovieByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = repository.findMoviesByQuery(query)
            searchMoviesLiveData.postValue(movies)
        }
    }

    fun onMovieClicked(movie: Movie) {
        // TODO handle navigation to details screen event
    }

    // Вызывается для очищения ресурсов
    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}