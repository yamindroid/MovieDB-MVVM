package com.ymo.ui.component.top_rated

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.Resource
import com.ymo.data.model.api.MovieItem
import com.ymo.data.model.api.MovieResponse
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.model.error.NETWORK_ERROR
import com.ymo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(
    private val dataRepositoryHelper: DataRepositoryHelper
) : BaseViewModel() {
    private val movies = MutableLiveData<Resource<MovieResponse>>()
    val movieLiveData: LiveData<Resource<MovieResponse>> get() = movies

    private val addFavoriteStatus = MutableLiveData<Resource<Unit>>()
    val addFavoriteStatusLiveData: LiveData<Resource<Unit>> get() = addFavoriteStatus

    fun loadMovies(page:Int) {
        if (network.isConnected) {
            viewModelScope.launch {
                movies.postValue(Resource.loading(null))
                try {
                    movies.postValue(Resource.success(dataRepositoryHelper.loadTopRatedMovies(page)))
                } catch (e: HttpException) {
                    movies.postValue(Resource.error(errorManager.getHttpError(e).description, null))
                } catch (e: Exception) {
                    movies.postValue(Resource.error(e.localizedMessage ?: e.message!!, null))
                }

            }
        } else movies.postValue(
            Resource.error(
                errorManager.getError(NETWORK_ERROR).description,
                null
            )
        )
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