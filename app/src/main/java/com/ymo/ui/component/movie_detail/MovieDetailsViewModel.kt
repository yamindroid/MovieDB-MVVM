package com.ymo.ui.component.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.Resource
import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.model.error.NETWORK_ERROR
import com.ymo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val dataRepositoryHelper: DataRepositoryHelper
) : BaseViewModel() {
    private val movieDetail = MutableLiveData<Resource<MovieDetail>>()
    val movieDetailLiveData: LiveData<Resource<MovieDetail>> get() = movieDetail

    private val addFavoriteStatus = MutableLiveData<Resource<Unit>>()
    val addFavoriteStatusLiveData: LiveData<Resource<Unit>> get() = addFavoriteStatus

    private val removeFavoriteStatus = MutableLiveData<Resource<Int>>()
    val removeFavoriteStatusLiveData: LiveData<Resource<Int>> get() = removeFavoriteStatus

    fun loadMovieDetail(movieId: Int) {
        if (network.isConnected) {
            viewModelScope.launch {
                movieDetail.postValue(Resource.loading(null))
                try {
                    movieDetail.postValue(
                        Resource.success(
                            dataRepositoryHelper.loadMovieDetails(
                                movieId
                            )
                        )
                    )
                } catch (e: HttpException) {
                    movieDetail.postValue(
                        Resource.error(
                            errorManager.getHttpError(e).description,
                            null
                        )
                    )
                } catch (e: Exception) {
                    movieDetail.postValue(Resource.error(e.localizedMessage ?: e.message!!, null))
                }

            }
        } else movieDetail.postValue(
            Resource.error(
                errorManager.getError(NETWORK_ERROR).description,
                null
            )
        )
    }


    fun addFavoriteMovie(movieDetail: MovieDetail) {
        viewModelScope.launch {
            addFavoriteStatus.postValue(Resource.loading(null))
            try {
                addFavoriteStatus.value = Resource.success(
                    dataRepositoryHelper.addFavoriteMovie(
                        FavoriteMovie(
                            id = movieDetail.id,
                            posterPath = movieDetail.posterPath,
                            releaseDate = movieDetail.releaseDate ?: "0000-00-00",
                            title = movieDetail.title,
                            voteAverage = movieDetail.voteAverage,
                            voteCount = movieDetail.voteCount,
                            genres = movieDetail.genres
                        )
                    )
                )
            } catch (e: Exception) {
                addFavoriteStatus.postValue(Resource.error(e.localizedMessage ?: e.message!!, null))
            }

        }
    }

    fun removeFavoriteMovie(movieDetail: MovieDetail) {
        viewModelScope.launch {
            removeFavoriteStatus.postValue(Resource.loading(null))
            try {
                removeFavoriteStatus.value=  Resource.success(
                    dataRepositoryHelper.deleteFavoriteMovieById(movieDetail.id)
                )
            } catch (e: Exception) {
                removeFavoriteStatus.postValue(Resource.error(e.localizedMessage ?: e.message!!, null))
            }

        }
    }
}

