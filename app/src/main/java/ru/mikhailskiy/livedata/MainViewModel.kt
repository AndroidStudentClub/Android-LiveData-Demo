package ru.mikhailskiy.livedata

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.mikhailskiy.livedata.data.Movie
import ru.mikhailskiy.livedata.data.MovieRepository
import ru.mikhailskiy.livedata.ui.DataLoadingState

class MainViewModel(private val repository: MovieRepository) : ViewModel() {

    private var debouncePeriod: Long = 500
    private var searchJob: Job? = null

    val movieLoadingStateLiveData = MutableLiveData<DataLoadingState>()


    // 1. Создаём MutableLiveData для передачи данных в View
    private lateinit var _searchMoviesLiveData: LiveData<List<Movie>>

    private val _searchFieldTextLiveData = MutableLiveData<String>()
    private val _popularMoviesLiveData = MutableLiveData<List<Movie>>()

    val moviesMediatorData = MediatorLiveData<List<Movie>>()

    init {
        _searchMoviesLiveData = Transformations.switchMap(_searchFieldTextLiveData) {
            fetchMovieByQuery(it)
        }

        // 1
        moviesMediatorData.addSource(_popularMoviesLiveData) {
            moviesMediatorData.value = it
        }

        // 2
        moviesMediatorData.addSource(_searchMoviesLiveData) {
            moviesMediatorData.value = it
        }
    }

    fun getNowPlayingMovies() {
        if (_popularMoviesLiveData.value.isNullOrEmpty()) {
            fetchNowPlayingMovies()
        }
    }

    // 2. Вызывается из View для передачи строки поиска в сетевой запрос
    fun onSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            if (query.length > 2) {
                _searchFieldTextLiveData.value = query
            }
        }
    }


    private fun fetchNowPlayingMovies() {
        movieLoadingStateLiveData.value = DataLoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = repository.fetchNowPlayingMovies()
                _popularMoviesLiveData.postValue(movies)
                movieLoadingStateLiveData.postValue(DataLoadingState.LOADED)
            } catch (e: Exception) {
                movieLoadingStateLiveData.postValue(DataLoadingState.ERROR)
            }
        }
    }

    private fun fetchMovieByQuery(query: String): LiveData<List<Movie>> {
        val liveData = MutableLiveData<List<Movie>>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1 setValue c помощью которого мы отправляем статус о загрузке через LiveData можно вызывать только UI-потоке
                // поэтому тут Dispatchers.Main
                withContext(Dispatchers.Main) {
                    movieLoadingStateLiveData.value = DataLoadingState.LOADING
                }

                val movies = repository.findMoviesByQuery(query)
                liveData.postValue(movies)

                // 2 После того, как запрос выполнится - оповещаем UI о том, что
                // загрузка выполнилась
                movieLoadingStateLiveData.postValue(DataLoadingState.LOADED)
            } catch (e: Exception) {
                // 3 В случае ошибки - отправляем статус об ошибке
                movieLoadingStateLiveData.postValue(DataLoadingState.ERROR)
            }
        }
        return liveData
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