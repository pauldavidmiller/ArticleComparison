package com.paulmiller.project2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import java.lang.Exception

class CurrentArticles : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currentarticles) //set tweets layout


        //retrieve intent information
        //val currentLocation: String? = intent.getStringExtra("LOCATION")
        val searchTerm = intent.getStringExtra(SearchArticles.Intent_Constants.search_term)
        /*val year = intent.getStringExtra(SearchArticles.Intent_Constants.selected_year)
        val month = intent.getStringExtra(SearchArticles.Intent_Constants.selected_month)
        val day = intent.getStringExtra(SearchArticles.Intent_Constants.selected_day)*/
        // NO DATE BECAUSE IT IS CURRENT - USE CURRENT WEEK

        title = "CURRENT - Search Term: $searchTerm"

        recyclerView = findViewById(R.id.recyclerViewCurrent)

        //set the recyclerView direction to vertical (the default)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get articles
        getCurrentArticles(searchTerm)

    }

    fun getCurrentArticles(searchTerm: String){
        // Networking is required to happen on background thread, especially since server response
        // times can be long if the user has poor connection
        doAsync {
            val articlesManager = ArticlesManager()

            try {

                val articles: MutableList<Article> = articlesManager.getCurrentArticlesFromNYT(
                    searchTerm = searchTerm
                )

                runOnUiThread {
                    val adapter = ArticlesAdapter(articles)
                    recyclerView.adapter = adapter
                }

            } catch (exception: Exception){
                runOnUiThread {
                    Toast.makeText(this@CurrentArticles, "failed to retrieve Tweets", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}