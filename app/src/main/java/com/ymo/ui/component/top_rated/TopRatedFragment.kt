package com.ymo.ui.component.top_rated

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.api.MovieItem
import com.ymo.databinding.FragmentTopRatedBinding
import com.ymo.ui.MovieAdapter
import com.ymo.ui.MovieLoadStateAdapter
import com.ymo.ui.component.movie_detail.MovieDetailsActivity
import com.ymo.utils.showSnackbar
import com.ymo.utils.showToast
import com.ymo.utils.toGone
import com.ymo.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopRatedFragment : Fragment(), MovieAdapter.OnClickedListener {

    private var _binding: FragmentTopRatedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TopRatedViewModel by viewModels()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopRatedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUIs()
        setupObservers()
    }

    private fun setupUIs() {
        viewModel.checkInternet()
        binding.rvTopRated.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
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
                rvTopRated.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnNoData.isVisible = loadState.source.refresh is LoadState.Error
            }
        }
    }

    private fun setupObservers() {
        viewModel.movieLiveData.observe(viewLifecycleOwner, ::moviesHandler)
        viewModel.addFavoriteStatusLiveData.observe(viewLifecycleOwner, ::addFavStatusHandler)
        viewModel.noInternetLiveData.observe(viewLifecycleOwner, ::noInternetHandler)
    }

    private fun noInternetHandler(noInternet: String) {
        binding.root.showSnackbar(noInternet, Snackbar.LENGTH_LONG)
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

    private fun moviesHandler(resource: PagingData<MovieItem>) {
        movieAdapter.submitData(lifecycle, resource)
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
        binding.progressBar.toGone()
    }

    private fun showLoadingView() {
        binding.progressBar.toVisible()
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