package ru.mikhailskiy.livedata.domain.usecase

import io.reactivex.Single
import ru.mikhailskiy.livedata.data.MovieDto
import ru.mikhailskiy.livedata.data.database.Movie
import ru.mikhailskiy.livedata.data.repository.MovieRemoteRepository
import javax.inject.Inject
import javax.inject.Named

class MovieUseCase @Inject constructor(
    @Named("local") val repository: MovieInterface,
    @Named("fake") val fakeRepository: MovieInterface
) {

    fun getMovies(): List<Movie> {
        return repository.getMovies()
    }
}

class MovieRemoteUseCase @Inject constructor(private val repository: MovieRemoteRepository) {

    fun getMovies(): Single<List<MovieDto>> {
        return repository.getMovies()
    }
}

interface MovieInterface {
    fun getMovies(): List<Movie>
}