package ru.mikhailskiy.livedata.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert
    fun save(movie: List<Movie>)

    @Delete
    fun delete(movie: Movie)

    @Query("SELECT * FROM Movies")
    fun getMovies(): List<Movie>
}
