package com.example.moviedb.model


import com.google.gson.annotations.SerializedName

data class VideoRequest(
    @SerializedName("videos")
    val videos: Videos
)