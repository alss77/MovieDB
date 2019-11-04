package com.example.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.moviedb.adapter.MyAdapter
import com.example.moviedb.model.MovieItem
import com.example.moviedb.model.MovieList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val movieList : MutableList<MovieItem> = mutableListOf()
    private var page = 1
    private val pageLimit = 500
    private var isLoading = false
    private val url = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&language=fr&api_key=b44d9e6bf8714009dda8266271780346&page="
    private lateinit var myAdapter: MyAdapter
    private lateinit var linearLayout: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myAdapter = MyAdapter(movieList)
        linearLayout = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayout
        recyclerView.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
        recyclerView.adapter = myAdapter
        AndroidNetworking.initialize(this)
        loadPage()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayout.childCount
                val pastVisibleItem = linearLayout.findFirstCompletelyVisibleItemPosition()
                val total = myAdapter.itemCount

                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItem >= total)) {
                        page++
                        loadPage()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    fun loadPage() {
        if (page <= pageLimit) {
            isLoading = true
            progress_bar.visibility = View.VISIBLE
            AndroidNetworking.get(url + page)
                .build()
                .getAsObject(MovieList::class.java, object : ParsedRequestListener<MovieList> {
                    override fun onResponse(response: MovieList) {
                        movieList.addAll(response.results)
                        myAdapter.notifyDataSetChanged()
                        println("Chargement de la page $page")
                    }
                    override fun onError(anError: ANError?) {
                    }
                })
        }
        Handler().postDelayed({
            isLoading = false
            progress_bar.visibility = View.GONE
        }, 1000)
    }
}