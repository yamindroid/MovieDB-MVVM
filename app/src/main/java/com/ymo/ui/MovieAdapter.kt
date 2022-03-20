package com.ymo.ui

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ymo.R
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieItem
import com.ymo.utils.getLocalTimeFromUnix
import com.ymo.utils.inflate
import com.ymo.utils.loadFromUrl
import kotlinx.android.synthetic.main.movie_card.view.*

private const val TAG = "MovieAdapter"

class MovieAdapter(
    private val listener: OnClickedListener
) : PagingDataAdapter<MovieItem, MovieAdapter.MovieViewHolder>(MovieModelComparator) {
    private var genres: List<GenresItem>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent.inflate(R.layout.movie_card))
    }

    fun setGenres(genres: List<GenresItem>) {
        this.genres = genres
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position)!!, listener)
    }

    interface OnClickedListener {
        fun onPosterClicked(movieItem: MovieItem)
        fun onFavoriteClicked(movieItem: MovieItem)//todo remove later
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            movieItem: MovieItem,
            listener: OnClickedListener
        ) {
            with(itemView)
            {
                val url = "https://image.tmdb.org/t/p/original/" + movieItem.posterPath
                var genreName = ""
                iv_poster.loadFromUrl(url)
                tv_title.text = movieItem.title
                tv_vote.text = resources.getString(R.string.votes_ui, movieItem.voteCount)
                tv_vote_average.text = movieItem.voteAverage.toString()
                val generesById: Map<Int?, GenresItem>? = genres?.associateBy { it.id }
                movieItem.genreIds?.filter { generesById?.get(it) != null }
                    ?.map { genreId ->
                        generesById?.get(genreId)?.let { genresItem ->
                            genreName += genresItem.name + ",";
                        }
                    }
                genreName =
                    if (genreName.length > 0) genreName.substring(0, genreName.length - 1) else ""
                tv_genre.text = genreName
                tv_date.text =
                    if (movieItem.releaseDate?.isEmpty() == true) "00/00/0000" else movieItem.releaseDate?.let {
                        getLocalTimeFromUnix(it)
                    }

                setOnClickListener {
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