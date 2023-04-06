package org.arjix.gunet2.network

// THIS IS JUST TESTING CODE, DON'T TAKE IT SERIOUSLY

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.droidsonroids.jspoon.annotation.Selector
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.http.GET
import retrofit2.http.Query

class BlogPage {
    @Selector(".post") lateinit var posts: List<Post>
}

class Post {
    @Selector(".post-content > h2 > a") lateinit var title: String
    @Selector(".excerpt") lateinit var excerpt: String
    @Selector(".post-featured-image > a > img", attr = "data-lazy-src") lateinit var imageUrl: String
    @Selector(".post-category > a") lateinit var tags: List<String>
}

interface BlogService {
    @GET("blog")
    fun getBlogPage(@Query("page") pageNumber: Int): Call<BlogPage>
}

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
