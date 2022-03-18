package com.ymo.ui.component.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.Resource
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val dataRepositoryHelper: DataRepositoryHelper
) : BaseViewModel() {
    private val movies = MutableLiveData<Resource<List<FavoriteMovie>>>()
    val movieLiveData: LiveData<Resource<List<FavoriteMovie>>> get() = movies

    private val removeFavoriteStatus = MutableLiveData<Resource<Int>>()
    val removeFavoriteStatusLiveData: LiveData<Resource<Int>> get() = removeFavoriteStatus

    fun loadFavoriteMovies() {
        viewModelScope.launch {
            movies.postValue(Resource.loading(null))
            try {
                movies.postValue(Resource.success(dataRepositoryHelper.getFavoriteMovies()))
            } catch (e: Exception) {
                movies.postValue(Resource.error(e.localizedMessage ?: e.message!!, null))
            }

        }
    }

    fun removeFavoriteMovie(favoriteMovie: FavoriteMovie) {
        viewModelScope.launch {
            removeFavoriteStatus.postValue(Resource.loading(null))
            try {
                removeFavoriteStatus.value = Resource.success(
                    dataRepositoryHelper.deleteFavoriteMovieById(favoriteMovie.id)
                )
            } catch (e: Exception) {
                removeFavoriteStatus.postValue(
                    Resource.error(
                        e.localizedMessage ?: e.message!!,
                        null
                    )
                )
            }

        }
    }

}
