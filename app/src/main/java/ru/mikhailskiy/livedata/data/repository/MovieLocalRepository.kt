package ru.mikhailskiy.livedata.data.repository

import io.reactivex.Single
import ru.mikhailskiy.livedata.data.MovieDto
import ru.mikhailskiy.livedata.data.database.Movie
import ru.mikhailskiy.livedata.data.database.MovieDao
import ru.mikhailskiy.livedata.data.network.MovieApiInterface
import ru.mikhailskiy.livedata.domain.usecase.MovieInterface
import javax.inject.Inject

class MovieLocalRepository @Inject constructor(private val dao: MovieDao):MovieInterface{

    override fun getMovies(): List<Movie> {
        return dao.getMovies()
    }

}

class FakeRemoteRepository @Inject constructor(private val api: MovieApiInterface):MovieInterface {

    override fun getMovies(): List<Movie> {
        return listOf()
    }

}

class MovieRemoteRepository @Inject constructor(private val api: MovieApiInterface) {

    fun getMovies(): Single<List<MovieDto>> {
        return api.getNowPlayingMovies().map { it->it.results }
    }

}