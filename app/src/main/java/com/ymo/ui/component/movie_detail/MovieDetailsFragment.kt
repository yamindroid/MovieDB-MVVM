package com.ymo.ui.component.movie_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ymo.R
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.api.MovieDetail
import com.ymo.databinding.FragmentMovieDetailBinding
import com.ymo.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

    private var isFav = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadMovieDetail(args.movieId)
        isFav = args.isFav
        if (isFav) showFavorite()
//        requireActivity()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.movieDetailLiveData.observe(viewLifecycleOwner, ::movieDetailHandler)
        viewModel.addFavoriteStatusLiveData.observe(viewLifecycleOwner, ::addFavStatusHandler)
        viewModel.removeFavoriteStatusLiveData.observe(viewLifecycleOwner, ::removeFavStatusHandler)
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
        binding.tvVote.text = resources.getString(R.string.voted_ui, movieDetail.voteCount)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}