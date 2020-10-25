package ru.mikhailskiy.livedata.domain.usecase

import io.reactivex.Single
import ru.mikhailskiy.livedata.data.MovieDto
import ru.mikhailskiy.livedata.data.database.Movie
import ru.mikhailskiy.livedata.data.repository.MovieRemoteRepository
import ru.mikhailskiy.livedata.data.repository.MovieRepository
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val repository: MovieRepository) {

    fun getMovies(): List<Movie> {
        return repository.getMovies()
    }
}

class MovieRemoteUseCase @Inject constructor(private val repository: MovieRemoteRepository) {

    fun getMovies(): Single<List<MovieDto>> {
        return repository.getMovies()
    }
}