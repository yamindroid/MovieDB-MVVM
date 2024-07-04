package com.ymo.ui.component.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ymo.R
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.databinding.MovieCardBinding
import com.ymo.utils.getLocalTimeFromUnix
import com.ymo.utils.loadFromUrl

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
        return FavoriteViewHolder(MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface OnClickedListener {
        fun onPosterClicked(favoriteMovie: FavoriteMovie)
        fun onFavoriteClicked(favoriteMovie: FavoriteMovie)
    }
}

class FavoriteViewHolder(private val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        favoriteMovie: FavoriteMovie,
        listener: FavoriteMoviesAdapter.OnClickedListener
    ) {
        with(binding)
        {

            val url = "https://image.tmdb.org/t/p/original/" + favoriteMovie.posterPath
            var genres= ""
            favoriteMovie.genres?.map {
                genres += it?.name + ",";
            }
            genres =
                if (genres.length > 0) genres.substring(0, genres.length - 1) else ""
            tvGenre.text = genres
            ivPoster.loadFromUrl(url)
            tvTitle.text = favoriteMovie.title
            tvVote.text = tvVote.context.getString(R.string.votes_ui, favoriteMovie.voteCount)
            tvVoteAverage.text = favoriteMovie.voteAverage.toString()
            tvDate.text =
                if (favoriteMovie.releaseDate?.isEmpty() == true) "00/00/0000" else favoriteMovie.releaseDate?.let {
                    getLocalTimeFromUnix(it)
                }

            root.setOnClickListener {
                listener.onPosterClicked(favoriteMovie)
            }
        }

    }
}
