package fr.epf.matmob.filmroll.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

/**
 * Stores the data about a film.
 */
data class Film(
    val tmdbId: Int,
    val title: String,
    val originalTitle: String,
    val synopsis: String,
    val releaseDate: String,
    val duration: Duration,
    val posterPath: String,
    val backdropPath: String,
    val voteScore: Float,
    val voteCount: Int,
    val collection: String,
    val budget: Long,
    val genres: List<Genre>,
    val homepage: String,
    val imdbId: String,
    val originalLanguage: String,
    val productionCompanies: List<String>,
    val productionCountries: List<String>,
    val languages: List<String>,
    val status: String,
    val adult: Boolean
) {
    fun toLiteFilm(): LiteFilm {
        return LiteFilm(
            tmdbId,
            title,
            synopsis,
            releaseDate,
            originalLanguage,
            posterPath,
            voteScore,
            voteCount,
            adult
        )
    }
}

/**
 * A cutback version for the Film class, with the fields present in the
 * search results of the TMDB API.
 */
@Entity
data class LiteFilm(
    @PrimaryKey val tmdbId: Int,
    val title: String,
    val synopsis: String,
    val releaseDate: String,
    val originalLanguage: String,
    val posterPath: String,
    val voteScore: Float,
    val voteCount: Int,
    val adult: Boolean,
)