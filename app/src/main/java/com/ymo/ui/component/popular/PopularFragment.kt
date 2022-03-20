package com.ymo.ui.component.popular

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieItem
import com.ymo.databinding.FragmentPopularBinding
import com.ymo.ui.MovieAdapter
import com.ymo.ui.MovieLoadStateAdapter
import com.ymo.ui.component.movie_detail.MovieDetailsActivity
import com.ymo.utils.showSnackbar
import com.ymo.utils.showToast
import com.ymo.utils.toGone
import com.ymo.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : Fragment(), MovieAdapter.OnClickedListener {

    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PopularViewModel by viewModels()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUIs()
        setupObservers()
    }

    private fun setupUIs() {
        viewModel.checkInternet()
        binding.rvPopular.apply {
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
                rvPopular.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnNoData.isVisible = loadState.source.refresh is LoadState.Error
            }
        }
    }

    private fun setupObservers() {
        viewModel.genresLiveData.observe(viewLifecycleOwner, ::genresHandler)
        viewModel.noInternetLiveData.observe(viewLifecycleOwner, ::noInternetHandler)
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
                viewModel.movieLiveData.observe(viewLifecycleOwner, ::moviesHandler)
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