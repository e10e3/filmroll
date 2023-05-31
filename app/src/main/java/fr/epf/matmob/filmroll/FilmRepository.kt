package fr.epf.matmob.filmroll

import fr.epf.matmob.filmroll.model.Film
import fr.epf.matmob.filmroll.network.TmdbService

/**
 * Allows to seamlessly access the data, may it be from the APi or the database.
 */
class FilmRepository(private val APIService: TmdbService) {
    suspend fun getMovie(id: Int): Film = APIService.getMovie(id).toFilm()
}