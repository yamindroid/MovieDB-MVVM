package com.ymo.ui.component.movie_detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.ymo.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDetailsActivityTest
{
    @get:Rule
    var activityRule = ActivityTestRule(MovieDetailsActivity::class.java)

    @Test
    fun poster_isDisplayed(){
        onView(withId(R.id.iv_poster)).check(matches(isDisplayed()))
    }

    @Test
    fun title_isDisplayed(){
        onView(withId(R.id.movie_title)).check(matches(isDisplayed()))
    }

    @Test
    fun release_date_isDisplayed(){
        onView(withId(R.id.tv_release)).check(matches(isDisplayed()))
    }

    @Test
    fun vote_count_isDisplayed(){
        onView(withId(R.id.tv_vote_count)).check(matches(isDisplayed()))
    }
    @Test
    fun spoken_languages_isDisplayed(){
        onView(withId(R.id.tv_spoken_language)).check(matches(isDisplayed()))
    }

    @Test
    fun watch_movie_isDisplayed(){
        onView(withId(R.id.tv_watch_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_watch_movie)).perform(ViewActions.click())
    }

}