package fr.epf.matmob.filmroll.model

import java.net.URL
import java.util.Calendar
import kotlin.time.Duration

/**
 * Stores the data about a film.
 */
data class Film(
    val tmdbId: Int,
    val title: String,
    val originalTitle: String,
    val synopsis: String,
    val releaseDate: Calendar,
    val duration: Duration,
    val posterPath: String,
    val backdropPath: String,
    val voteScore: Float,
    val voteCount: Int,
    val collection: String,
    val budget: Int,
    val genres: List<String>,
    val homepage: URL,
    val imdbId: String,
    val originalLanguage: String,
    val productionCompanies: List<String>,
    val productionCountries: List<String>,
    val languages: List<String>,
    val status: String,
    val adult: Boolean,
)

/**
 * A cutback version for the Film class, with the fields present in the
 * search results of the TMDB API.
 */
data class LiteFilm(
    val tmdbId: Int,
    val title: String,
    val synopsis: String,
    val releaseDate: Calendar,
    val originalLanguage: String,
    val posterPath: String,
    val voteScore: Float,
    val voteCount: Int,
    val adult: Boolean,
)