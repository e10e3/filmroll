package fr.epf.matmob.filmroll.network

import fr.epf.matmob.filmroll.BuildConfig
import fr.epf.matmob.filmroll.network.model.ResponseFilm
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TmdbService {
    @Headers(
        "Accept: application/json", "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}"
    )
    @GET("movie/{id}")
    suspend fun getMovie(
        @Path("id") id: Int
    ): ResponseFilm
}