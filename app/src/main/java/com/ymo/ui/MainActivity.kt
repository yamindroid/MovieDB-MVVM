package com.ymo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.ymo.R
import com.ymo.databinding.ActivityMainBinding
import com.ymo.databinding.FragmentPopularBinding
import com.ymo.ui.component.favorites.FavoritesActivity
import com.ymo.ui.component.movie_detail.MovieDetailsActivity
import com.ymo.ui.component.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

//todo clean code


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var moviesArray: Array<String>
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        moviesArray = arrayOf(
            getString(R.string.now_playing),
            getString(R.string.popular),
            getString(R.string.top_rated),
            getString(R.string.upcoming)
        )
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = moviesArray[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            R.id.action_favorite -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}