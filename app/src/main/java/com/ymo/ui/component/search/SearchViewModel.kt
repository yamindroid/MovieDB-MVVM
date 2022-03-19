package com.ymo.ui.component.search

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.Resource
import com.ymo.data.model.api.MovieItem
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.model.error.NETWORK_ERROR
import com.ymo.data.paging.SearchPagingSource
import com.ymo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataRepositoryHelper: DataRepositoryHelper
) : BaseViewModel() {

    private val currentQuery = MutableLiveData<String>()

    private val noInternet = MutableLiveData<String>()
    val noInternetLiveData: LiveData<String> get() = noInternet

    private val addFavoriteStatus = MutableLiveData<Resource<Unit>>()
    val addFavoriteStatusLiveData: LiveData<Resource<Unit>> get() = addFavoriteStatus


    fun checkInternet() {
        if (!network.isConnected)
            noInternet.postValue(errorManager.getError(NETWORK_ERROR).description)
    }
    val movieLiveData = currentQuery.switchMap { queryString ->
        val result = Pager(PagingConfig(PAGE_SIZE)) { SearchPagingSource(dataRepositoryHelper,queryString) }
       result.liveData.cachedIn(viewModelScope)
    }
    fun searchMoviesByQuery(query: String) {
        currentQuery.value = query
    }


    fun addFavoriteMovie(movieItem: MovieItem) {
        viewModelScope.launch {
            addFavoriteStatus.postValue(Resource.loading(null))
            try {
                addFavoriteStatus.value =  Resource.success(
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

}