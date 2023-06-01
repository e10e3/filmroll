package fr.epf.matmob.filmroll.network.model

import fr.epf.matmob.filmroll.model.ExtendedFilmInfo
import fr.epf.matmob.filmroll.model.Film
import fr.epf.matmob.filmroll.model.LiteFilm
import fr.epf.matmob.filmroll.model.Person
import java.net.URL
import java.util.Calendar
import kotlin.streams.toList
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val TAG = "ApiResponseModel"

data class ResponseExtendedFilm(
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
    val credits: ResponseCredits,
    val recommendations: ResponseRecommendations
) {
    fun toExtendedFilmInfo(): ExtendedFilmInfo {
        val cal = Calendar.getInstance()
        return ExtendedFilmInfo(
            Film(
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
            ),
            credits.cast.stream().map { it.toPerson() }.toList(),
            credits.crew.stream().map { it.toPerson() }.toList(),
            recommendations.results.stream().map { it.toLiteFilm() }.toList()
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

data class ResponseSearchResults(
    val page: Int,
    val results: List<ResponseShortFilm>,
    val total_pages: Int,
    val total_results: Int
)

data class ResponseShortFilm(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int,
) {
    fun toLiteFilm(): LiteFilm {
        val cal = Calendar.getInstance()
        return LiteFilm(
            id,
            title,
            overview,
            cal,
            original_language,
            poster_path ?: "",
            vote_average,
            vote_count,
            adult
        )
    }
}

data class ResponseRecommendations(
    val page: Int,
    val results: List<ResponseShortFilm>,
    val total_pages: Int,
    val total_results: Int
)

data class ResponseCastMember(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Float,
    val profile_path: String,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val order: Int
) {
    fun toPerson(): Person = Person(id, name, character, profile_path ?: "")
}

data class ResponseCrewMember(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Float,
    val profile_path: String,
    val credit_id: String,
    val department: String,
    val job: String
) {
    fun toPerson(): Person = Person(id, name, job, profile_path ?: "")
}

data class ResponseCredits(
    val cast: List<ResponseCastMember>, val crew: List<ResponseCrewMember>
)
