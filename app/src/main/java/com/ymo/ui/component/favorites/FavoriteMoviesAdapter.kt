package com.ymo.ui.component.favorites

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ymo.R
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.utils.inflate
import com.ymo.utils.loadFromUrl
import kotlinx.android.synthetic.main.activity_movie_detail.view.*
import kotlinx.android.synthetic.main.movie_card.view.*
import kotlinx.android.synthetic.main.movie_card.view.iv_favorite_movie
import kotlinx.android.synthetic.main.movie_card.view.iv_poster

class FavoriteMoviesAdapter(
    private val listener: OnClickedListener
) : ListAdapter<FavoriteMovie, FavoriteViewHolder>(
    object : DiffUtil.ItemCallback<FavoriteMovie>() {
        override fun areItemsTheSame(
            oldItem: FavoriteMovie,
            newItem: FavoriteMovie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavoriteMovie,
            newItem: FavoriteMovie
        ): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(parent.inflate(R.layout.movie_card))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface OnClickedListener {
        fun onPosterClicked(favoriteMovie: FavoriteMovie)
        fun onFavoriteClicked(favoriteMovie: FavoriteMovie)
    }
}

class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        favoriteMovie: FavoriteMovie,
        listener: FavoriteMoviesAdapter.OnClickedListener
    ) {
        with(itemView)
        {
            val url = "https://image.tmdb.org/t/p/original/" + favoriteMovie.posterPath
            iv_poster.loadFromUrl(url)
            tv_title.text = favoriteMovie.title
            tv_vote.text = resources.getString(R.string.voted_ui,favoriteMovie.voteCount)
            iv_favorite_movie.setImageResource(R.drawable.ic_favorite_red_light)
            setOnClickListener {
                listener.onPosterClicked(favoriteMovie)
            }

            iv_favorite_movie.setOnClickListener {
                iv_favorite_movie.setImageResource(R.drawable.ic_favorite_border_red_light)
                listener.onFavoriteClicked(favoriteMovie)
            }
        }

    }
}
