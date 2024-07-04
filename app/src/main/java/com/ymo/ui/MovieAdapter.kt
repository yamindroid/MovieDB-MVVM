package com.ymo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ymo.R
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieItem
import com.ymo.databinding.MovieCardBinding
import com.ymo.utils.getLocalTimeFromUnix
import com.ymo.utils.loadFromUrl

private const val TAG = "MovieAdapter"

class MovieAdapter(
    private val listener: OnClickedListener
) : PagingDataAdapter<MovieItem, MovieAdapter.MovieViewHolder>(MovieModelComparator) {
    private var genres: List<GenresItem>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun setGenres(genres: List<GenresItem>) {
        this.genres = genres
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position)!!, listener)
    }

    interface OnClickedListener {
        fun onPosterClicked(movieItem: MovieItem)
    }

    inner class MovieViewHolder(val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movieItem: MovieItem,
            listener: OnClickedListener
        ) {
            with(binding)
            {
                val url = "https://image.tmdb.org/t/p/original/" + movieItem.posterPath
                var genreName = ""
                ivPoster.loadFromUrl(url)
                tvTitle.text = movieItem.title
                tvVote.text = tvVote.context.getString(R.string.votes_ui, movieItem.voteCount)
                tvVoteAverage.text = movieItem.voteAverage.toString()
                val generesById: Map<Int?, GenresItem>? = genres?.associateBy { it.id }
                movieItem.genreIds?.filter { generesById?.get(it) != null }
                    ?.map { genreId ->
                        generesById?.get(genreId)?.let { genresItem ->
                            genreName += genresItem.name + ",";
                        }
                    }
                genreName =
                    if (genreName.length > 0) genreName.substring(0, genreName.length - 1) else ""
                tvGenre.text = genreName
                tvDate.text =
                    if (movieItem.releaseDate?.isEmpty() == true) "00/00/0000" else movieItem.releaseDate?.let {
                        getLocalTimeFromUnix(it)
                    }

                root.setOnClickListener {
                    listener.onPosterClicked(movieItem)
                }
            }

        }
    }


    companion object {
        private val MovieModelComparator = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}