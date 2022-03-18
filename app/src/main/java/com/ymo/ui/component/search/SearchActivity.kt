package com.ymo.ui.component.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ymo.R
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.api.MovieItem
import com.ymo.data.model.api.MovieResponse
import com.ymo.databinding.ActivitySearchBinding
import com.ymo.ui.component.movie_detail.MovieDetailsActivity
import com.ymo.ui.component.upcoming.MoviesAdapter
import com.ymo.utils.showSnackbar
import com.ymo.utils.showToast
import com.ymo.utils.toGone
import com.ymo.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), MoviesAdapter.OnClickedListener {

    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val favoriteMoviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUIs()
        setupObservers()
    }

    private fun setUpUIs() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoriteMoviesAdapter
        }

    }

    private fun setupObservers() {
        viewModel.movieLiveData.observe(this, ::moviesHandler)
        viewModel.addFavoriteStatusLiveData.observe(this, ::addFavStatusHandler)
    }

    private fun addFavStatusHandler(resource: Resource<Unit>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let {
                showDataView(true)
                binding.root.showToast("Added to Favorites", Toast.LENGTH_SHORT)
            }
            Status.ERROR -> {
                showDataView(false)
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }
    }

    private fun moviesHandler(resource: Resource<MovieResponse>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let {
                showDataView(it.results?.isNotEmpty()!!)
                favoriteMoviesAdapter.submitList(it.results)
            }
            Status.ERROR -> {
                showDataView(false)
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }

    }

    override fun onPosterClicked(movieItem: MovieItem) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        val bundle = Bundle().apply {
            putInt(MOVIE_ID, movieItem.id)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onFavoriteClicked(movieItem: MovieItem) {
        viewModel.addFavoriteMovie(movieItem)
    }

    private fun showDataView(show: Boolean) {
        binding.btnNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.loaderView.toGone()
    }

    private fun showLoadingView() {
        binding.loaderView.toVisible()
        binding.btnNoData.toGone()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isIconified = false
        searchView.queryHint = getString(R.string.query_hint);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchMoviesByQuery(query, 1)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
        searchView.setOnCloseListener {
            finish()
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val MOVIE_ID = "MOVIE_ID"
    }
}