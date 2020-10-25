package ru.mikhailskiy.livedata.data

import com.google.gson.annotations.SerializedName

data class MoviesResponse (
    var page: Int,
    var results: List<MovieDto>,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("total_pages")
    var totalPages: Int
)

