package com.ymo.data.favorites

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.ymo.data.local.db.AppDatabase
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.utils.TestUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FavoriteMovieDaoTest {

    private lateinit var mDatabase: AppDatabase

    @Before
    fun createDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getTargetContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        mDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun isMovieListEmpty() = runBlocking {
        assertEquals(0, mDatabase.favoriteMovieDao().getFavoriteMovies().size)
    }

    @Test
    @Throws(Exception::class)
    fun addFavoriteMovie() = runBlocking {
        val favoriteMovie: FavoriteMovie = TestUtil.createFavoriteMovie(1)
        val addedFavoriteMovie = mDatabase.favoriteMovieDao().addFavoriteMovie(favoriteMovie)
        assertNotNull(addedFavoriteMovie)
    }

    @Test
    @Throws(Exception::class)
    fun getFavoriteMovies() = runBlocking {
        val favoriteMovies = TestUtil.createFavoriteMovies(5)
        favoriteMovies.forEach {
            mDatabase.favoriteMovieDao().addFavoriteMovie(it)
        }
        val loadedFavoriteMovies = mDatabase.favoriteMovieDao().getFavoriteMovies()
        assertThat(favoriteMovies, equalTo(loadedFavoriteMovies))
    }

    @Test
    @Throws(Exception::class)
    fun deleteFavoriteMovieById() = runBlocking {
        val deletedFavoriteMovie = mDatabase.favoriteMovieDao().deleteFavoriteMovieById(7)
        val loadedFavoriteMovies = mDatabase.favoriteMovieDao().getFavoriteMovies()
        assertEquals(false,loadedFavoriteMovies.contains(deletedFavoriteMovie))
    }

}
