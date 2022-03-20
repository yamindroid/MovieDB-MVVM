package com.ymo.data.genres

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.ymo.data.local.db.AppDatabase
import com.ymo.data.model.api.GenresItem
import com.ymo.utils.TestUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GenreDaoTest {

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
    fun addGenre() = runBlocking {
        val genre: GenresItem = TestUtil.createGenre(1)
        val addedGenre = mDatabase.genreDao().addGenre(genre)
        assertNotNull(addedGenre)
    }

    @Test
    @Throws(Exception::class)
    fun getGenres() = runBlocking {
        val genres = TestUtil.createGenres(7)
        genres.forEach {
            mDatabase.genreDao().addGenre(it)
        }
        val loadedGenres = mDatabase.genreDao().getGenres()
        assertThat(genres, equalTo(loadedGenres))
    }

}
