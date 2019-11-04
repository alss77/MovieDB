package com.example.moviedb.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.MovieDetail
import com.example.moviedb.R
import com.example.moviedb.model.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item_layout.view.*

class MyAdapter(private val movieList: MutableList<MovieItem>) : RecyclerView.Adapter<MyHolder>() {

    private val apiImageUrl = "https://image.tmdb.org/t/p/w300"
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        context = parent.context
        return MyHolder(
            LayoutInflater.from(context).inflate(
                R.layout.movie_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val data = movieList[position]
        val movieTitle = holder.itemView.movieTitle
        val movieDate = holder.itemView.movieDate
        val moviePoster = holder.itemView.moviePoster
        val movieVote =  holder.itemView.movieVote

        movieTitle.text = data.title
        movieDate.text = "Sorti le : ${data.releaseDate}"
        movieVote.text = "Vote : ${data.voteAverage}/10"
        Picasso.get()
            .load("$apiImageUrl${data.posterPath}")
            .into(moviePoster)

        holder.itemView.setOnClickListener{
            Toast.makeText(context, movieTitle.text, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MovieDetail::class.java)
            intent.putExtra("title", data.title)
            intent.putExtra("date", "Sorti le : ${data.releaseDate}")
            intent.putExtra("overview", data.overview)
            intent.putExtra("poster", data.posterPath)
            intent.putExtra("video", data.id.toString())
            context.startActivity(intent)
        }
    }
}
