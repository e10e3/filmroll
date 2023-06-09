package fr.epf.matmob.filmroll.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.epf.matmob.filmroll.model.FavouriteFilm
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Query("SELECT * FROM favourite_film WHERE film_id = :id")
    suspend fun getById(id: Int): FavouriteFilm

    @Query("SELECT COUNT(1) FROM favourite_film WHERE film_id = :id and is_favourite = 1")
    suspend fun isFavourite(id: Int): Boolean

    @Query("SELECT * FROM favourite_film WHERE is_favourite=1")
    fun getFavourites(): Flow<List<FavouriteFilm>>

    @Query("SELECT * FROM favourite_film")
    fun getAll(): Flow<List<FavouriteFilm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: FavouriteFilm)

    @Delete
    suspend fun delete(film: FavouriteFilm)
}