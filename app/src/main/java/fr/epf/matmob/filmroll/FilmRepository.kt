package fr.epf.matmob.filmroll

import fr.epf.matmob.filmroll.model.ExtendedFilmInfo
import fr.epf.matmob.filmroll.model.LiteFilm
import fr.epf.matmob.filmroll.network.TmdbService
import kotlin.streams.toList

/**
 * Allows to seamlessly access the data, may it be from the APi or the database.
 */
class FilmRepository(private val APIService: TmdbService) {
    suspend fun getFilm(id: Int): ExtendedFilmInfo = APIService.getFilm(id).toExtendedFilmInfo()

    suspend fun searchFilm(query: String): List<LiteFilm> =
        APIService.searchFilm(query).results.stream().map { it.toLiteFilm() }.toList()

    suspend fun getPopularFilms(): List<LiteFilm> =
        APIService.getPopularFilms().results.stream().map { it.toLiteFilm() }.toList()
}