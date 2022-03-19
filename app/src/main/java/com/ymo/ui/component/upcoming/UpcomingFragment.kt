package com.ymo.ui.component.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.api.MovieItem
import com.ymo.data.model.api.MovieResponse
import com.ymo.databinding.FragmentUpcomingBinding
import com.ymo.ui.component.movie_detail.MovieDetailsActivity
import com.ymo.utils.showSnackbar
import com.ymo.utils.showToast
import com.ymo.utils.toGone
import com.ymo.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFragment : Fragment(), MoviesAdapter.OnClickedListener {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by viewModels()

    private val favoriteMoviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUIs()
        setupObservers()
    }

    private fun setupUIs() {
        viewModel.loadMovies(1)
        binding.rvUpcomingMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoriteMoviesAdapter
        }

        binding.btnNoData.setOnClickListener {
            binding.loaderView.toVisible()
            binding.btnNoData.toGone()
            viewModel.loadMovies(1)
        }

    }

    private fun setupObservers() {
        viewModel.movieLiveData.observe(viewLifecycleOwner, ::moviesHandler)
        viewModel.addFavoriteStatusLiveData.observe(viewLifecycleOwner, ::addFavStatusHandler)
    }

    private fun addFavStatusHandler(resource: Resource<Unit>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let {
                showDataView(true)
                binding.root.showToast("Added to Favorites", Toast.LENGTH_SHORT)
            }
            Status.ERROR -> {
                showDataView(true)
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }
    }

    private fun moviesHandler(resource: Resource<MovieResponse>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let {
                showDataView(true)
                favoriteMoviesAdapter.submitList(it.results)
            }
            Status.ERROR -> {
                showDataView(false)
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }

    }

    override fun onPosterClicked(movieItem: MovieItem) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val MOVIE_ID = "MOVIE_ID"
    }
}