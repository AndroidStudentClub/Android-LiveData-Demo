package ru.mikhailskiy.livedata

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.livedata.adapters.MovieItem
import ru.mikhailskiy.livedata.data.MovieRepository
import ru.mikhailskiy.livedata.data.network.MovieApiInterface
import ru.mikhailskiy.livedata.domain.usecase.MovieRemoteUseCase
import ru.mikhailskiy.livedata.domain.usecase.MovieUseCase
import ru.mikhailskiy.livedata.ui.DataLoadingState
import ru.mikhailskiy.livedata.ui.afterTextChanged
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    @Inject lateinit var car: Car

    @Inject
    lateinit var useCase: MovieUseCase

    @Inject
    lateinit var useCaseRemote: MovieRemoteUseCase

//    @Inject lateinit var movieApiInterface: MovieApiInterface

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as DemoApp).component.inject(this)
        // 2. Создание ViewModel, используя провайдер и передав экземпляр фабрики
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        // 3. После того, как текст изменился - вызываем метод ViewModel
        search_toolbar.search_edit_text.afterTextChanged { it ->
            mainViewModel.onSearchQuery(it.toString())
        }

        // 4. Подписываемся на LiveData для получения результатов
        mainViewModel.moviesMediatorData.observe(this, Observer { list ->
            val moviesList = list.map { MovieItem(it) }.toList()
            // 5. Обновляем UI
            // Необходимо очистить адаптер
            adapter.clear()
            movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }
        })

        mainViewModel.getNowPlayingMovies()

        mainViewModel.movieLoadingStateLiveData.observe(this, Observer {
            onMovieLoadingStateChanged(it)
        })

        val movies = useCaseRemote.getMovies()
        movies
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("W7", it?.let { it.toString() } ?: "null")
            }, {
                Log.d("W7", it.toString())
            })
    }

    private fun onMovieLoadingStateChanged(state: DataLoadingState) {
        progress_circular.visibility =
            if (state == DataLoadingState.LOADING) View.VISIBLE else View.GONE
    }


}
