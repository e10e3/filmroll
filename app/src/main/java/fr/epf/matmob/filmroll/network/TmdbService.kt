package fr.epf.matmob.filmroll.network

import fr.epf.matmob.filmroll.BuildConfig
import fr.epf.matmob.filmroll.network.model.ResponseExtendedFilm
import fr.epf.matmob.filmroll.network.model.ResponseSearchResults
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {
    @Headers(
        "Accept: application/json", "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}"
    )
    @GET("movie/{id}?append_to_response=credits,recommendations")
    suspend fun getFilm(
        @Path("id") id: Int, @Query("language") language: String = "en-GB"
    ): ResponseExtendedFilm

    @Headers(
        "Accept: application/json", "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}"
    )
    @GET("search/movie")
    suspend fun searchFilm(
        @Query("query") query: String, @Query("language") language: String = "en-GB", @Query("region") region: String = "GB"
    ): ResponseSearchResults

    @Headers(
        "Accept: application/json", "Authorization: Bearer ${BuildConfig.TMDB_API_TOKEN}"
    )
    @GET("movie/popular")
    suspend fun getPopularFilms(
        @Query("language") language: String = "en-GB", @Query("region") region: String = "GB"
    ): ResponseSearchResults
}