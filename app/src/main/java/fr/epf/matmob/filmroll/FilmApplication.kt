package fr.epf.matmob.filmroll

import android.app.Application
import fr.epf.matmob.filmroll.network.TmdbService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FilmApplication : Application() {
    private val client = OkHttpClient.Builder().build()
    private val retrofit =
        Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(
            MoshiConverterFactory.create()
        ).client(client).build()
    private val service = retrofit.create(TmdbService::class.java)
    val repository by lazy { FilmRepository(service) }
}