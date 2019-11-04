package com.example.moviedb

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.moviedb.model.VideoRequest
import com.squareup.picasso.Picasso

class MovieDetail : AppCompatActivity() {

    private val youtubeUrl = "https://www.youtube.com/embed/"
    private val apiUrl = "https://api.themoviedb.org/3/movie/"
    private val apiImageUrl = "https://image.tmdb.org/t/p/w300"
    private val token = "?&api_key=b44d9e6bf8714009dda8266271780346"
    private val videoUrl = "&append_to_response=videos&include_adult=false"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_layout)

        var intent = intent
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val overview = intent.getStringExtra("overview")
        val poster = intent.getStringExtra("poster")
        val videoId = intent.getStringExtra("video")

        val movieTitle = findViewById<TextView>(R.id.detailTitle)
        movieTitle.text = title
        val movieDate = findViewById<TextView>(R.id.detailDate)
        movieDate.text = date
        val movieOverview = findViewById<TextView>(R.id.detailOverview)
        movieOverview.text = overview
        val moviePoster = findViewById<ImageView>(R.id.detailPoster)
        Picasso.get()
            .load("$apiImageUrl$poster")
            .into(moviePoster)
        loadWebView(videoId)
    }

    private fun loadWebView(videoId: String) {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("$apiUrl$videoId$token$videoUrl")
            .build()
            .getAsObject(VideoRequest::class.java, object : ParsedRequestListener<VideoRequest> {
                override fun onResponse(response: VideoRequest) {
                    val key = response.videos.results[0].key
                    val movieVideo = findViewById<WebView>(R.id.detailVideo)
                    movieVideo.settings.javaScriptEnabled = true
                    movieVideo.settings.domStorageEnabled = true
                    movieVideo.loadUrl("$youtubeUrl$key")
                    movieVideo.webChromeClient = WebChromeClient()
                }
                override fun onError(anError: ANError?) {
                }
            })
    }
}