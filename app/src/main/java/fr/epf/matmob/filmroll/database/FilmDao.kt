package fr.epf.matmob.filmroll.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.epf.matmob.filmroll.model.FavouriteFilm
import fr.epf.matmob.filmroll.model.LiteFilm
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Query("SELECT * FROM favourite_film WHERE film_id = :id")
    suspend fun getFavouriteById(id: Int): FavouriteFilm

    @Query("SELECT * FROM litefilm WHERE tmdbId = :id")
    suspend fun getLiteFilmById(id: Int): LiteFilm

    @Query("SELECT COUNT(1) FROM favourite_film WHERE film_id = :id and is_favourite = 1")
    suspend fun isFavourite(id: Int): Boolean

    @Query("SELECT * FROM favourite_film WHERE is_favourite=1")
    fun getFavourites(): Flow<List<FavouriteFilm>>

    @Query("SELECT * FROM litefilm WHERE tmdbId IN (SELECT film_id FROM favourite_film WHERE is_favourite=1) ")
    fun getFavouriteLiteFilms(): Flow<List<LiteFilm>>

    @Query("SELECT * FROM favourite_film")
    fun getAllFavourites(): Flow<List<FavouriteFilm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(film: FavouriteFilm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiteFilm(film: LiteFilm)

    @Delete
    suspend fun deleteFavourite(film: FavouriteFilm)
}