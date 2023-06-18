package fr.epf.matmob.filmroll.state

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import fr.epf.matmob.filmroll.model.ExtendedFilmInfo
import fr.epf.matmob.filmroll.model.FavouriteFilm
import fr.epf.matmob.filmroll.model.LiteFilm
import kotlinx.coroutines.launch

private const val TAG = "FilmViewModel"

/**
 * Describes the unfolding of a network request.
 */
enum class RequestState {
    /** The request has been sent, but the result is not yet available */
    LOADING,

    /** The request has ended, and the result is available without error */
    SUCCESS,

    /** The request has ended, but no result was found */
    NOT_FOUND,

    /** The request has ended, but an error occurred */
    ERROR
}

class FilmViewModel(private val repository: FilmRepository) : ViewModel() {
    private val _filmInfo = MutableLiveData<ExtendedFilmInfo>()
    val filmInfo: LiveData<ExtendedFilmInfo> = _filmInfo
    private val _filmInfoStatus = MutableLiveData<RequestState>()
    val filmInfoStatus: LiveData<RequestState> = _filmInfoStatus

    private val _foundFilms = MutableLiveData<List<LiteFilm>>()
    val foundFilms: LiveData<List<LiteFilm>> = _foundFilms
    private val _foundFilmsStatus = MutableLiveData<RequestState>()
    val foundFilmsStatus: LiveData<RequestState> = _foundFilmsStatus

    private val _popularFilms = MutableLiveData<List<LiteFilm>>()
    val popularFilms: LiveData<List<LiteFilm>> = _popularFilms
    private val _popularFilmsStatus = MutableLiveData<RequestState>()
    val popularFilmsStatus: LiveData<RequestState> = _popularFilmsStatus

    val favouriteFilms: LiveData<List<FavouriteFilm>> = repository.favouriteFilms.asLiveData()

    val favouriteLiteFilms: LiveData<List<LiteFilm>> = repository.favouriteLiteFilms.asLiveData()

    private val _isFilmFavourite = MutableLiveData<Boolean>()
    val isFilmFavourite: LiveData<Boolean> = _isFilmFavourite

    fun getFilm(id: Int) {
        Log.i(TAG, "getFilm: request for film #$id")
        _filmInfoStatus.value = RequestState.LOADING
        viewModelScope.launch {
            try {
                val film = repository.getFilm(id)
                _filmInfo.value = film
                Log.d(TAG, "getFilm: $film")
                _filmInfoStatus.value = RequestState.SUCCESS
            } catch (e: retrofit2.HttpException) {
                Log.w(TAG, "getFilm: ${e.message}", e)
                _filmInfoStatus.value = if (e.code() == 404) {
                    RequestState.NOT_FOUND
                } else {
                    RequestState.ERROR
                }
            } catch (e: Exception) {
                Log.e(TAG, "getFilm: ${e.message}", e)
                _filmInfoStatus.value = RequestState.ERROR
            }
        }
    }

    fun searchFilm(query: String) {
        _foundFilmsStatus.value = RequestState.LOADING
        viewModelScope.launch {
            try {
                Log.d(TAG, "searchFilm: API call with param $query")
                val foundFilms = repository.searchFilm(query)
                Log.d(TAG, "searchFilm: $foundFilms")
                _foundFilms.value = foundFilms
                _foundFilmsStatus.value = RequestState.SUCCESS
            } catch (e: Exception) {
                Log.e(TAG, "searchFilm: ${e.message}", e)
                _foundFilmsStatus.value = RequestState.ERROR
            }
        }
    }

    fun getPopularFilms() {
        _popularFilmsStatus.value = RequestState.LOADING
        viewModelScope.launch {
            try {
                val popularFilms = repository.getPopularFilms()
                Log.d(TAG, "getPopularFilms: $popularFilms")
                _popularFilms.value = popularFilms
                _popularFilmsStatus.value = RequestState.SUCCESS
            } catch (e: Exception) {
                Log.e(TAG, "getPopularFilms: ${e.message}", e)
                _popularFilmsStatus.value = RequestState.ERROR
            }
        }
    }

    fun insertFavourite(film: FavouriteFilm) {
        viewModelScope.launch {
            repository.insertFavourite(film)
        }
    }

    fun insertLiteFilm(film: LiteFilm) {
        viewModelScope.launch {
            repository.insertLiteFilm(film)
        }
    }

    fun isFilmFavourite(filmId: Int) {
        viewModelScope.launch {
            _isFilmFavourite.value = repository.isFilmFavourite(filmId)
        }
    }
}

class FilmViewModelFactory(private val repository: FilmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return FilmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
