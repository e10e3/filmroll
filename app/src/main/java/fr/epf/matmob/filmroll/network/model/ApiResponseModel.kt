package fr.epf.matmob.filmroll.network.model

import fr.epf.matmob.filmroll.model.Film
import java.net.URL
import java.util.Calendar
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val TAG = "ApiResponseModel"

data class ResponseFilm(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: ResponseCollection,
    val budget: Int,
    val genres: List<ResponseGenres>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String,
    val production_companies: List<ResponseProductionCompany>,
    val production_countries: List<ResponseProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<ResponseSpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int,
) {
    fun toFilm(): Film {
        val cal = Calendar.getInstance()
        return Film(
            id,
            title,
            original_title,
            overview,
            cal,
            runtime.toDuration(DurationUnit.MINUTES),
            poster_path ?: "",
            backdrop_path ?: "",
            vote_average,
            vote_count,
            belongs_to_collection?.name ?: "",
            budget,
            genres.map { it.name }.toList(),
            URL(homepage.ifEmpty { "http://example.org" }),
            imdb_id,
            original_language,
            production_companies.map { it.name }.toList(),
            production_countries.map { it.name }.toList(),
            spoken_languages.map { it.iso_639_1 }.toList(),
            status,
            adult
        )
    }
}

data class ResponseCollection(
    val id: Int, val name: String, val poster_path: String, val backdrop_path: String
)

data class ResponseGenres(val id: Int, val name: String)
data class ResponseProductionCompany(
    val id: Int, val logo_path: String, val name: String, val origin_country: String
)

data class ResponseProductionCountry(val iso_3166_1: String, val name: String)
data class ResponseSpokenLanguage(
    val english_name: String, val iso_639_1: String, val name: String
)