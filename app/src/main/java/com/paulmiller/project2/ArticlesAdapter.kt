package com.paulmiller.project2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticlesAdapter(val tweets: List<Article>) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    // the adapter needs to render a new row and needs to know what xml file to use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //layout inflation (read and parse XML file and return a reference to the root layout)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_article, parent, false)
        return ViewHolder(view)
    }

    // the adapter has a row that's ready to be rendered and needs the content filled in
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTweet = tweets[position]
        holder.web_url.setText(currentTweet.web_url)
        holder.web_url.isClickable = true

        holder.headline.setText(currentTweet.snippet)
        holder.first_paragraph.setText(currentTweet.lead_paragraph)

        // Load icon from currentTweet.iconURL
        /*Picasso.get().setIndicatorsEnabled(true)

        if (currentTweet.iconURL.isNotEmpty()) {
            Picasso.get()
                .load(currentTweet.iconURL)
                .into(holder.icon)
        }*/
    }

    // return # of rows you expect your list to have
    override fun getItemCount(): Int {
        return tweets.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val web_url: TextView = itemView.findViewById(R.id.web_url)
        val headline: TextView = itemView.findViewById(R.id.headline)
        val first_paragraph: TextView = itemView.findViewById(R.id.first_paragraph)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }
}