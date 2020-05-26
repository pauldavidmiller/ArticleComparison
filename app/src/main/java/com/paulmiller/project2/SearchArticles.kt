package com.paulmiller.project2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*




class SearchArticles : AppCompatActivity() {


    private lateinit var search: EditText
    private lateinit var buttonOld: Button
    private lateinit var buttonNew: Button
    private lateinit var date: EditText

    object Intent_Constants {
        val search_term = "search_term"
        val selected_year = "selected_year"
        val selected_month = "selected_month"
        val selected_day = "selected_day"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searcharticles)

        Log.d("SearchArticles", "onCreate() called")

        //initialize variables
        search = findViewById(R.id.searchText)
        date = findViewById(R.id.datePicker)
        buttonOld = findViewById(R.id.searchOldButton)
        buttonNew = findViewById(R.id.searchNewButton)


        // On Click Listeners
        buttonNew.setOnClickListener {
            Log.d("SearchArticles", "new button onclick called")

            val text = search.text.toString()

            // Go to recycler view
            val intent: Intent = Intent(this, CurrentArticles::class.java) //go from here to TweetsActivity class
            intent.putExtra(Intent_Constants.search_term, text)
            // no date because it's current - take current week
            startActivity(intent) //execute intent
        }

        buttonOld.setOnClickListener {
            Log.d("SearchArticles", "old button onClick called")

            //if nothing input
            if (date.text.toString().isEmpty()){
                val monthSelected = "1"
                val yearSelected = "1945"

                // Go to recycler view
                val intent: Intent = Intent(this, OldArticles::class.java)
                intent.putExtra(Intent_Constants.selected_year, yearSelected)
                intent.putExtra(Intent_Constants.selected_month, monthSelected)
                startActivity(intent)
            } else {
                val dateSplit: List<String> = date.text.toString().split("/")

                var monthSelected = dateSplit.get(0)
                // No leading zeros
                if (monthSelected[0] == '0'){
                    monthSelected = monthSelected[1].toString()
                }
                Log.d("SearchArticles", "month selected: $monthSelected")

                val yearSelected = dateSplit.get(1)

                // Go to recycler view
                val intent: Intent = Intent(this, OldArticles::class.java)
                intent.putExtra(Intent_Constants.selected_year, yearSelected)
                intent.putExtra(Intent_Constants.selected_month, monthSelected)
                startActivity(intent)
            }

        }


    }

    fun getDate(milliSeconds: Long, dateFormat: String): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    override fun onResume() {
        super.onResume()
        Log.d("LoginActivity", "onResume() called")
    }

    override fun onStop() {
        Log.d("LoginActivity", "onStop() called");
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("LoginActivity", "onDestroy() called");
        super.onDestroy()
    }

}