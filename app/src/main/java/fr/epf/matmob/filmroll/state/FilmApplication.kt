package fr.epf.matmob.filmroll.state

import android.app.Application
import fr.epf.matmob.filmroll.database.FilmDatabase
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
    val database by lazy { FilmDatabase.getDatabase(this) }
    val repository by lazy { FilmRepository(service, database.filmDao()) }
}