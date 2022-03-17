package com.ymo.ui.component.upcoming

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ymo.R
import com.ymo.data.model.api.MovieItem
import com.ymo.utils.inflate
import com.ymo.utils.loadFromUrl
import kotlinx.android.synthetic.main.movie_card.view.*

class UpcomingMoviesAdapter(
    private val listener: OnClickedListener
) : ListAdapter<MovieItem, PopularViewHolder>(
    object : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(
            oldItem: MovieItem,
            newItem: MovieItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieItem,
            newItem: MovieItem
        ): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(parent.inflate(R.layout.movie_card))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface OnClickedListener {
        fun onPosterClicked(movieItem: MovieItem)
        fun onFavoriteClicked(movieItem: MovieItem)
    }
}

class PopularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        movieItem: MovieItem,
        listener: UpcomingMoviesAdapter.OnClickedListener
    ) {
        with(itemView)
        {
            val url = "https://image.tmdb.org/t/p/w500/" + movieItem.posterPath
            iv_poster.loadFromUrl(url)
            tv_title.text = movieItem.title
            tv_vote.text = resources.getString(R.string.voted_ui,movieItem.voteCount)

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