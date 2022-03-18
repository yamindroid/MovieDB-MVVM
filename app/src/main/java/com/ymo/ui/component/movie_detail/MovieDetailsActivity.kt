package com.ymo.ui.component.movie_detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ymo.R
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.api.MovieDetail
import com.ymo.databinding.ActivityMovieDetailBinding
import com.ymo.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val IMAGE_URL = "https://image.tmdb.org/t/p/original/"

    private var isFav = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUIs()
        setupObservers()
    }

    private fun setUpUIs() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val movieId = intent?.extras?.getInt(MOVIE_ID) ?: return
        viewModel.loadMovieDetail(movieId)
    }

    private fun setupObservers() {
        viewModel.movieDetailLiveData.observe(this, ::movieDetailHandler)
        viewModel.addFavoriteStatusLiveData.observe(this, ::addFavStatusHandler)
        viewModel.removeFavoriteStatusLiveData.observe(this, ::removeFavStatusHandler)
    }

    private fun removeFavStatusHandler(resource: Resource<Int>) {
        when (resource.status) {
            Status.LOADING -> binding.loaderView.toVisible()
            Status.SUCCESS -> resource.data?.let {
                binding.loaderView.toGone()
                binding.root.showToast("Removed from Favorites", Toast.LENGTH_SHORT)
            }
            Status.ERROR -> {
                binding.loaderView.toGone()
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }
    }

    private fun addFavStatusHandler(resource: Resource<Unit>) {
        when (resource.status) {
            Status.LOADING -> binding.loaderView.toVisible()
            Status.SUCCESS -> resource.data?.let {
                binding.loaderView.toGone()
                binding.root.showToast("Added to Favorites", Toast.LENGTH_SHORT)
            }
            Status.ERROR -> {
                binding.loaderView.toGone()
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }
    }

    private fun movieDetailHandler(resource: Resource<MovieDetail>) {
        when (resource.status) {
            Status.LOADING -> binding.loaderView.toVisible()
            Status.SUCCESS -> resource.data?.let {
                binding.loaderView.toGone()
                bindDetailData(it)
            }
            Status.ERROR -> {
                binding.loaderView.toGone()
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }

    }

    private fun bindDetailData(movieDetail: MovieDetail) {
        binding.movieTitle.text = movieDetail.title

        binding.overview.text = movieDetail.overview
        binding.orTitle.text = movieDetail.originalTitle

        binding.orLan.text =
            if (movieDetail.originalLanguage == "en") "English" else movieDetail.originalLanguage
        binding.txRelease.text =
            getLocalTimeFromUnix(movieDetail.releaseDate ?: "0000-00-00")
        binding.tvDate.text =
            getLocalTimeFromUnix(movieDetail.releaseDate ?: "0000-00-00")
        binding.ivPoster.loadFromUrl(IMAGE_URL + movieDetail.posterPath)
        binding.tvVoteCount.text = resources.getString(R.string.voted_ui, movieDetail.voteCount)
        binding.ivFavoriteMovie.setOnClickListener {
            if (isFav) {
                removeFavorite()
                viewModel.removeFavoriteMovie(movieDetail)
                isFav = false
            } else {
                showFavorite()
                viewModel.addFavoriteMovie(movieDetail)
                isFav = true
            }
        }

    }

    private fun showFavorite() {
        binding.ivFavoriteMovie.setImageResource(R.drawable.ic_favorite_red_light)
    }

    private fun removeFavorite() {
        binding.ivFavoriteMovie.setImageResource(R.drawable.ic_favorite_border_red_light)
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