package com.ymo.ui.component.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ymo.R
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.databinding.ActivityFavoritesBinding
import com.ymo.ui.component.movie_detail.MovieDetailsActivity
import com.ymo.utils.showSnackbar
import com.ymo.utils.showToast
import com.ymo.utils.toGone
import com.ymo.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesActivity : AppCompatActivity(), FavoriteMoviesAdapter.OnClickedListener {

    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoriteMoviesAdapter: FavoriteMoviesAdapter by lazy {
        FavoriteMoviesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUIs()
        setupObservers()
    }

    private fun setUpUIs() {
        supportActionBar?.title = getString(R.string.favorites)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.loadFavoriteMovies()
        binding.rvFavoriteMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoriteMoviesAdapter
        }
        binding.btnNoData.setOnClickListener {
            binding.loaderView.toVisible()
            binding.btnNoData.toGone()
            viewModel.loadFavoriteMovies()
        }
    }

    private fun setupObservers() {
        viewModel.movieLiveData.observe(this, ::moviesHandler)
        viewModel.removeFavoriteStatusLiveData.observe(this, ::removeFavStatusHandler)
    }

    private fun removeFavStatusHandler(resource: Resource<Int>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let {
                showDataView(true)
                binding.root.showToast("Removed from Favorites", Toast.LENGTH_SHORT)
                viewModel.loadFavoriteMovies()
            }
            Status.ERROR -> {
                showDataView(true)
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }
    }

    private fun moviesHandler(resource: Resource<List<FavoriteMovie>>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let {
                if (it.isEmpty()) showDataView(false)
                else showDataView(true)
                favoriteMoviesAdapter.submitList(it)

            }
            Status.ERROR -> {
                showDataView(false)
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }

    }

    override fun onPosterClicked(favoriteMovie: FavoriteMovie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        val bundle = Bundle().apply {
            putInt(MOVIE_ID, favoriteMovie.id)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onFavoriteClicked(favoriteMovie: FavoriteMovie) {
        viewModel.removeFavoriteMovie(favoriteMovie)
    }

    private fun showDataView(show: Boolean) {
        binding.btnNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.loaderView.toGone()
    }

    private fun showLoadingView() {
        binding.loaderView.toVisible()
        binding.btnNoData.toGone()
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
