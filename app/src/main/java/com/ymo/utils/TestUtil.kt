package com.ymo.utils

import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.db.FavoriteMovie


object TestUtil {

    fun createFavoriteMovie(id: Int) = FavoriteMovie(
        id = id,
        title = "Movie 1"
    )

    fun createFavoriteMovies(size: Int): MutableList<FavoriteMovie> {
        val list = ArrayList<FavoriteMovie>(size)
        list.forEach {
            var favoriteMovie = FavoriteMovie(id =(list.indexOf(it) + 1) ,title ="Movie ${list.indexOf(it)}")
            list.add(favoriteMovie)
        }
        return list
    }

    fun createGenre(id:Int) = GenresItem(
        id=id,
        name = "Comedy"
    )

    fun createGenres(size: Int): MutableList<GenresItem> {
        val list = ArrayList<GenresItem>(size)
        list.forEach {
            var genre = GenresItem(id =(list.indexOf(it) + 1) ,name = "Genre ${list.indexOf(it)}")
            list.add(genre)
        }
        return list
    }

}
