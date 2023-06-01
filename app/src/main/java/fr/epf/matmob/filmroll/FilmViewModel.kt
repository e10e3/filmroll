package fr.epf.matmob.filmroll

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.epf.matmob.filmroll.model.Film
import kotlinx.coroutines.launch

private const val TAG = "FilmViewModel"

class FilmViewModel(private val repository: FilmRepository) : ViewModel() {
    private val _film = MutableLiveData<Film>()
    val film: LiveData<Film> = _film

    fun getFilm(id: Int) {
        viewModelScope.launch {
            try {
                val film = repository.getFilm(id)
                _film.value = film
                Log.d(TAG, "getFilm: $film")
            } catch (e: Exception) {
                Log.e(TAG, "getFilm: ${e.message}", e)
            }
        }
    }
}

class FilmViewModelFactory(private val repository: FilmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
