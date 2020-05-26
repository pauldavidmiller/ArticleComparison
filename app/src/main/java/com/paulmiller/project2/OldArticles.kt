package com.paulmiller.project2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import java.lang.Exception

class OldArticles : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oldarticles) //set tweets layout


        //retrieve intent information
        //val currentLocation: String? = intent.getStringExtra("LOCATION")
        val searchTerm = intent.getStringExtra(SearchArticles.Intent_Constants.search_term)
        val year = intent.getStringExtra(SearchArticles.Intent_Constants.selected_year)
        val month = intent.getStringExtra(SearchArticles.Intent_Constants.selected_month)
        // NO DATE BECAUSE IT IS CURRENT - USE CURRENT WEEK

        title = "OLD Articles: $month/$year"

        recyclerView = findViewById(R.id.recyclerViewOld)

        //set the recyclerView direction to vertical (the default)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get articles
        getOldArticles(month!!, year!!)

    }


    fun getOldArticles(month: String, year: String) {
        // Networking is required to happen on background thread, especially since server response
        // times can be long if the user has poor connection
        doAsync {
            val articlesManager = ArticlesManager()

            try {

                val articles: MutableList<Article> = articlesManager.getOldArticlesFromNYT(
                    month = month,
                    year = year
                )

                Log.d("OldArticles", articles.toString())

                runOnUiThread {
                    val adapter = ArticlesAdapter(articles)
                    recyclerView.adapter = adapter
                }

            } catch (exception: Exception){
                runOnUiThread {
                    Toast.makeText(this@OldArticles, "failed to retrieve Tweets", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}