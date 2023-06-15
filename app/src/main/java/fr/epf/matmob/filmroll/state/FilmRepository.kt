package fr.epf.matmob.filmroll.state

import fr.epf.matmob.filmroll.database.FilmDao
import fr.epf.matmob.filmroll.model.ExtendedFilmInfo
import fr.epf.matmob.filmroll.model.FavouriteFilm
import fr.epf.matmob.filmroll.model.LiteFilm
import fr.epf.matmob.filmroll.network.TmdbService
import kotlinx.coroutines.flow.Flow
import kotlin.streams.toList

/**
 * Allows to seamlessly access the data, may it be from the APi or the database.
 */
class FilmRepository(private val APIService: TmdbService, private val dao: FilmDao) {

    val favouriteFilms: Flow<List<FavouriteFilm>> = dao.getFavourites()

    val favouriteLiteFilms: Flow<List<LiteFilm>> = dao.getFavouriteLiteFilms()

    suspend fun getFilm(id: Int): ExtendedFilmInfo =
        APIService.getFilm(id, AppConfiguration.appLanguage).toExtendedFilmInfo()

    suspend fun searchFilm(query: String): List<LiteFilm> = APIService.searchFilm(
        query, AppConfiguration.appLanguage, AppConfiguration.appRegion
    ).results.stream().map { it.toLiteFilm() }.toList()

    suspend fun getPopularFilms(): List<LiteFilm> = APIService.getPopularFilms(
        AppConfiguration.appLanguage, AppConfiguration.appRegion
    ).results.stream().map { it.toLiteFilm() }.toList()

    suspend fun insertFavourite(film: FavouriteFilm) = dao.insertFavourite(film)

    suspend fun insertLiteFilm(film: LiteFilm) = dao.insertLiteFilm(film)

    suspend fun isFilmFavourite(id: Int): Boolean = dao.isFavourite(id)
}