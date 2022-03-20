package com.ymo.ui.component.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.Resource
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieItem
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.model.error.NETWORK_ERROR
import com.ymo.data.paging.NowPlayingPagingSource
import com.ymo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val dataRepositoryHelper: DataRepositoryHelper,
    private val pagingSource: NowPlayingPagingSource
) : BaseViewModel() {
    val movieLiveData: LiveData<PagingData<MovieItem>> get() = loadMovies()

    private val noInternet = MutableLiveData<String>()
    val noInternetLiveData: LiveData<String> get() = noInternet

    private val genres = MutableLiveData<Resource<List<GenresItem>>>()
    val genresLiveData: LiveData<Resource<List<GenresItem>>> get() = genres

    init {
        getGenres()
    }

    fun getGenres() {
        viewModelScope.launch {
            genres.postValue(Resource.loading(null))
            try {
                genres.postValue(Resource.success(dataRepositoryHelper.getGenres()))
            } catch (e: Exception) {
                genres.postValue(Resource.error(e.localizedMessage ?: e.message!!, null))
            }

        }
    }

    fun checkInternet() {
        if (!network.isConnected)
            noInternet.postValue(errorManager.getError(NETWORK_ERROR).description)
    }

    private fun loadMovies(): LiveData<PagingData<MovieItem>> {
        val result = Pager(PagingConfig(PAGE_SIZE)) { pagingSource }
        return result.liveData.cachedIn(viewModelScope)
    }



    companion object {
        private const val PAGE_SIZE = 20
    }

}