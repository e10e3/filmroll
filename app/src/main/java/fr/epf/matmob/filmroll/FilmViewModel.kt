package fr.epf.matmob.filmroll

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.epf.matmob.filmroll.model.ExtendedFilmInfo
import fr.epf.matmob.filmroll.model.LiteFilm
import kotlinx.coroutines.launch

private const val TAG = "FilmViewModel"

class FilmViewModel(private val repository: FilmRepository) : ViewModel() {
    private val _filmInfo = MutableLiveData<ExtendedFilmInfo>()
    val filmInfo: LiveData<ExtendedFilmInfo> = _filmInfo

    private val _foundFilms = MutableLiveData<List<LiteFilm>>()
    val foundFilms : LiveData<List<LiteFilm>> = _foundFilms

    fun getFilm(id: Int) {
        viewModelScope.launch {
            try {
                val film = repository.getFilm(id)
                _filmInfo.value = film
                Log.d(TAG, "getFilm: $film")
            } catch (e: Exception) {
                Log.e(TAG, "getFilm: ${e.message}", e)
            }
        }
    }

    fun searchFilm(query: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "searchMovie: API call with param $query")
                val foundFilms = repository.searchFilm(query)
                Log.d(TAG, "searchMovie: $foundFilms")
                _foundFilms.value = foundFilms
            } catch (e: Exception) {
                Log.e(TAG, "searchMovie: ${e.message}", e)
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
