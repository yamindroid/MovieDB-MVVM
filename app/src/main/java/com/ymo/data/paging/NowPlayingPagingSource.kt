package com.ymo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.model.api.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val PAGE_INDEX = 1

class NowPlayingPagingSource @Inject constructor(private val dataRepoHelper: DataRepositoryHelper) :
    PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
            val nextPage = params.key ?: PAGE_INDEX
           return try {
                val response = dataRepoHelper.loadNowPlayingMovies(nextPage)
                val photos = response.results
                LoadResult.Page(
                    data = photos,
                    prevKey = if (nextPage == PAGE_INDEX) null else nextPage - 1,
                    nextKey = if (photos.isEmpty()) null else nextPage + 1
                )
            } catch (exception: IOException) {
                LoadResult.Error(exception)
            } catch (exception: HttpException) {
                LoadResult.Error(exception)
            }

    }

        override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
            return null
        }
    }