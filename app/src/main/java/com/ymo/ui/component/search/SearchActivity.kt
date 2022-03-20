package com.ymo.ui.component.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ymo.R
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieItem
import com.ymo.databinding.ActivitySearchBinding
import com.ymo.ui.MovieAdapter
import com.ymo.ui.MovieLoadStateAdapter
import com.ymo.ui.component.movie_detail.MovieDetailsActivity
import com.ymo.utils.showSnackbar
import com.ymo.utils.showToast
import com.ymo.utils.toGone
import com.ymo.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), MovieAdapter.OnClickedListener {

    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(this)
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
        viewModel.checkInternet()
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { movieAdapter.retry() },
                footer = MovieLoadStateAdapter { movieAdapter.retry() }
            )
        }

        binding.btnNoData.setOnClickListener {
            viewModel.checkInternet()
            movieAdapter.retry()
        }

        movieAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnNoData.isVisible = loadState.source.refresh is LoadState.Error
            }
        }

    }

    private fun setupObservers() {
        viewModel.noInternetLiveData.observe(this, ::noInternetHandler)
        viewModel.genresLiveData.observe(this, ::genresHandler)
    }

    private fun noInternetHandler(noInternet: String) {
        binding.root.showSnackbar(noInternet, Snackbar.LENGTH_LONG)
    }

    private fun moviesHandler(resource: PagingData<MovieItem>) {
        movieAdapter.submitData(lifecycle, resource)
    }

    private fun genresHandler(resource: Resource<List<GenresItem>>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let {
                showDataView(resource.data.isNotEmpty())
                movieAdapter.setGenres(resource.data)
                viewModel.movieLiveData.observe(this, ::moviesHandler)
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
                    viewModel.searchMoviesByQuery(query)
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