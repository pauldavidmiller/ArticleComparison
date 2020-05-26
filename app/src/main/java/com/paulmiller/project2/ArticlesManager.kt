package com.paulmiller.project2

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ArticlesManager {

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()

        // Get logs from network traffic through interceptor
        // Set up our OkHttpClient instance to log all network traffic to LogCat
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        // Timeout if wait to long
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)

        okHttpClient = builder.build()
    }

    fun getCurrentArticlesFromNYT(searchTerm: String): MutableList<Article> {
        Log.d("ArticlesManager", "getCurrentArticlesFromNYT called")

        /*val ctx: Context = LoginActivity.getContext()
        val apiKey = ctx.getString(R.string.nyt_current_api_key)*/

        val apiKey = ""
        Log.d("ArticlesManager", "$apiKey")

        // SORT BY DATE, SEARCH TERM, NEWEST TO OLDEST, USING CURRENT WEEK
        //"https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=romney&facet_field=day_of_week&facet=true&begin_date=20120101&end_date=20120101&api-key=$api_key"
        //"https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=body:(\"$searchTerm\")&begin_date=$date&end_date=$date&api-key=$api_key"
        val request = Request.Builder()
            .url("https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=headline:(\"$searchTerm\")&sort=newest&api-key=$apiKey") //body/headline
            //.header("api_key", ctx.resources.getString(R.string.metroKey))
            .build()

        Log.d("ArticlesManager", "Before execute")

        val response = okHttpClient.newCall(request).execute() //blocks thread and waits, enqueue creates new thread

        Log.d("ArticlesManager", "After execute: $response")

        // Parse response
        val articles: MutableList<Article> = mutableListOf()
        val responseString: String? = response.body?.string()
        if (response.isSuccessful && !responseString.isNullOrEmpty()){
            val json: JSONObject = JSONObject(responseString)

            Log.d("ArticlesManager", "$json")

            val resp: JSONObject = json.getJSONObject("response")
            val docs: JSONArray = resp.getJSONArray("docs")

            var length = docs.length()
            if (length > 30){
                length = 30
            }

            for (i in 0 until length){
                val curr = docs.getJSONObject(i)
                val web_url = curr.getString("web_url")
                val snippet = curr.getString("snippet")
                val lead_paragraph = curr.getString("lead_paragraph")

                val article = Article(
                    web_url = web_url,
                    snippet = snippet,
                    lead_paragraph = lead_paragraph
                )

                // Add tweet
                articles.add(article)
            }
        }

        return articles
    }

    fun getOldArticlesFromNYT(month: String, year: String): MutableList<Article> {
        Log.d("ArticlesManager", "getOldArticlesFromNYT called")

        /*val ctx: Context = LoginActivity.getContext()
        val apiKey = ctx.getString(R.string.nyt_current_api_key)*/

        val apiKey = ""
        Log.d("ArticlesManager", "$apiKey")

        // SORT BY DATE, SEARCH TERM, NEWEST TO OLDEST, USING CURRENT WEEK
        val request = Request.Builder()
            .url("https://api.nytimes.com/svc/archive/v1/$year/$month.json?api-key=$apiKey")
            //.header("api_key", ctx.resources.getString(R.string.metroKey))
            .build()

        Log.d("ArticlesManager", "Before execute")

        val response = okHttpClient.newCall(request).execute() //blocks thread and waits, enqueue creates new thread

        Log.d("ArticlesManager", "After execute: $response")

        // Parse response
        val articles: MutableList<Article> = mutableListOf()
        val responseString: String? = response.body?.string()
        if (response.isSuccessful && !responseString.isNullOrEmpty()){
            val json: JSONObject = JSONObject(responseString)

            Log.d("ArticlesManager", "$json")

            val resp: JSONObject = json.getJSONObject("response")
            val docs: JSONArray = resp.getJSONArray("docs")

            var length = docs.length()
            if (length > 30){
                length = 30
            }

            for (i in 0 until length){
                val curr = docs.getJSONObject(i)
                val headline = curr.getJSONObject("headline")
                val mainheadline = headline.getString("main")

                val web_url = curr.getString("web_url")
                val snippet = curr.getString("snippet")
                //val lead_paragraph = curr.getString("lead_paragraph")

                val article = Article(
                    web_url = web_url,
                    snippet = mainheadline,
                    lead_paragraph = snippet
                )

                // Add tweet
                articles.add(article)
            }
        }

        return articles
    }

}