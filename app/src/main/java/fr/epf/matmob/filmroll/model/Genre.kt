package fr.epf.matmob.filmroll.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre(
    @PrimaryKey val genreId: Int,
    val name: String
)
