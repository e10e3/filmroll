package fr.epf.matmob.filmroll.model

import java.net.URL
import java.util.Calendar
import kotlin.time.Duration

/**
 * Stores the data about a film.
 */
data class Film(
    val tmdb_id: Int,
    val title: String,
    val original_title: String,
    val synopsis: String,
    val release_date: Calendar,
    val duration: Duration,
    val poster_path: String,
    val backdrop_path: String,
    val vote_score: Float,
    val vote_count: Int,
    val collection: String,
    val budget: Int,
    val genres: List<String>,
    val homepage: URL,
    val imdb_id: String,
    val original_language: String,
    val production_companies: List<String>,
    val production_countries: List<String>,
    val languages: List<String>,
    val status: String,
    val adult: Boolean,
)

/**
 * A cutback version for the Film class, with the fields present in the
 * search results of the TMDB API.
 */
data class LiteFilm(
    val tmdb_id: Int,
    val title: String,
    val synopsis: String,
    val release_date: Calendar,
    val original_language: String,
    val poster_path: String,
    val vote_score: Float,
    val vote_count: Int,
    val adult: Boolean,
)