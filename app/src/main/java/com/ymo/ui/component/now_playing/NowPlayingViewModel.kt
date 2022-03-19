package com.ymo.ui.component.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.Resource
import com.ymo.data.model.api.MovieItem
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.model.error.NETWORK_ERROR
import com.ymo.data.paging.NowPlayingPagingSource
import com.ymo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val dataRepositoryHelper: DataRepositoryHelper,
    private val pagingSource: NowPlayingPagingSource
) : BaseViewModel() {
    val movieLiveData: LiveData<PagingData<MovieItem>> get() = loadMovies()

    private val noInternet = MutableLiveData<String>()
    val noInternetLiveData: LiveData<String> get() = noInternet

    private val addFavoriteStatus = MutableLiveData<Resource<Unit>>()
    val addFavoriteStatusLiveData: LiveData<Resource<Unit>> get() = addFavoriteStatus

    fun checkInternet() {
        if (!network.isConnected)
            noInternet.postValue(errorManager.getError(NETWORK_ERROR).description)
    }

    private fun loadMovies(): LiveData<PagingData<MovieItem>> {
        val result = Pager(PagingConfig(PAGE_SIZE)) { pagingSource }
        return result.liveData.cachedIn(viewModelScope)
    }


    fun addFavoriteMovie(movieItem: MovieItem) {
        viewModelScope.launch {
            addFavoriteStatus.postValue(Resource.loading(null))
            try {
                addFavoriteStatus.value = Resource.success(
                    dataRepositoryHelper.addFavoriteMovie(
                        FavoriteMovie(
                            id = movieItem.id,
                            posterPath = movieItem.posterPath,
                            releaseDate = movieItem.releaseDate ?: "0000-00-00",
                            title = movieItem.title,
                            voteAverage = movieItem.voteAverage,
                            voteCount = movieItem.voteCount
                        )
                    )
                )
            } catch (e: Exception) {
                addFavoriteStatus.postValue(Resource.error(e.localizedMessage ?: e.message!!, null))
            }

        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}