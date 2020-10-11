package ru.mikhailskiy.livedata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.mikhailskiy.livedata.data.Movie
import ru.mikhailskiy.livedata.data.MovieRepository
import ru.mikhailskiy.livedata.ui.DataLoadingState

class MainViewModel(private val repository: MovieRepository) : ViewModel() {

    private var debouncePeriod: Long = 500
    private var searchJob: Job? = null

    val movieLoadingStateLiveData = MutableLiveData<DataLoadingState>()

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
            try {
                // 1 setValue c помощью которого мы отправляем статус о загрузке через LiveData можно вызывать только UI-потоке
                // поэтому тут Dispatchers.Main
                withContext(Dispatchers.Main) {
                    movieLoadingStateLiveData.value = DataLoadingState.LOADING
                }

                val movies = repository.findMoviesByQuery(query)
                searchMoviesLiveData.postValue(movies)

                // 2 После того, как запрос выполнится - оповещаем UI о том, что
                // загрузка выполнилась
                movieLoadingStateLiveData.postValue(DataLoadingState.LOADED)
            } catch (e: Exception) {
                // 3 В случае ошибки - отправляем статус об ошибке
                movieLoadingStateLiveData.postValue(DataLoadingState.ERROR)
            }
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