package org.arjix.gunet2.network

// THIS IS JUST TESTING CODE, DON'T TAKE IT SERIOUSLY

import pl.droidsonroids.jspoon.annotation.Selector
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.http.GET
import retrofit2.http.Query

class SearchPage {
    @Selector(".last_episodes .items > li") lateinit var results: List<Result>
}

class Result {
    @Selector(".name a") lateinit var title: String
}

interface GogoAnime {
    @GET("search.html")
    fun search(@Query("keyword") keyword: String) : Call<SearchPage>
}

suspend fun main(): List<String> {
    val searchPage = createRetrofit().create(GogoAnime::class.java).search("overlord").await()
    return searchPage.results.map { it.title }
}

fun createRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://gogoanime.llc/")
        .addConverterFactory(JspoonConverterFactory.create())
        .build()
