package fr.epf.matmob.filmroll.network

import fr.epf.matmob.filmroll.BuildConfig
import fr.epf.matmob.filmroll.network.model.ResponseFilm
import fr.epf.matmob.filmroll.network.model.ResponseSearchResults
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {
    @Headers(
        "Accept: application/json", "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}"
    )
    @GET("movie/{id}")
    suspend fun getFilm(
        @Path("id") id: Int
    ): ResponseFilm

    @Headers(
        "Accept: application/json", "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}"
    )
    @GET("search/movie")
    suspend fun searchFilm(
        @Query("query") query: String
    ): ResponseSearchResults
}