package com.ymo.ui

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ymo.R
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieItem
import com.ymo.utils.inflate
import com.ymo.utils.loadFromUrl
import kotlinx.android.synthetic.main.movie_card.view.*

class MovieAdapter(
    private val listener: OnClickedListener) : PagingDataAdapter<MovieItem, MovieAdapter.MovieViewHolder>(MovieModelComparator) {
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
        fun onFavoriteClicked(movieItem: MovieItem)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            movieItem: MovieItem,
            listener: OnClickedListener
        ) {
            with(itemView)
            {
                val url = "https://image.tmdb.org/t/p/original/" + movieItem.posterPath
                var genreName =""
                iv_poster.loadFromUrl(url)
                tv_title.text = movieItem.title
                tv_vote.text = resources.getString(R.string.voted_ui, movieItem.voteCount)
                if(genres!=null) {
                    val generesById: Map<Int, GenresItem> = genres!!.associateBy { it.id!! }
                 movieItem.genreIds?.filter { generesById[it] != null }
                      ?.map { genreId ->
                          generesById[genreId]?.let { genresItem ->
                              genreName+=genresItem.name+",";
                          }
                      }
                    genreName = genreName.substring(0, genreName.length - 1)
                    Log.e("/////", "bind: genreName..."+genreName )
                }

                setOnClickListener {
                    listener.onPosterClicked(movieItem)
                }

                iv_favorite_movie.setOnClickListener {
                    iv_favorite_movie.setImageResource(R.drawable.ic_favorite_red_light)
                    listener.onFavoriteClicked(movieItem)
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