package fr.epf.matmob.filmroll.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_film")
data class FavouriteFilm(
    @PrimaryKey @ColumnInfo(name = "film_id") val filmId: Int,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean
)
