package com.paulmiller.project2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var username: EditText

    private lateinit var password: EditText

    private lateinit var login: Button

    private lateinit var register: Button

    private lateinit var progressBar: ProgressBar

    object Constants {
        val INTENT_KEY_LOCATION = "LOCATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("LoginActivity", "onCreate() called")

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAuth = FirebaseAuth.getInstance()

        val preferences: SharedPreferences = getSharedPreferences(
            "android-tweets",
            Context.MODE_PRIVATE
        )

        //initialize variables
        username = findViewById(R.id.usernameLogin)
        password = findViewById(R.id.passwordLogin)
        login = findViewById(R.id.signin)
        register = findViewById(R.id.signup)
        progressBar = findViewById(R.id.progressBarSignIn)

        progressBar.isEnabled = false


        //listeners
        login.isEnabled = false // set to false initially
        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)


        // Get username and password preferences if saved before
        val savedUsername: String? = preferences.getString("username", "")
        val savedPassword: String? = preferences.getString("password", "")
        username.setText(savedUsername)
        password.setText(savedPassword)

        //can use lambda because it only requires 1 function
//        login.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })


        // Login listener
        login.setOnClickListener { //view: View ->
            Log.d("LoginActivity", "onClick() called")

            // Create a toast for example
            //Toast.makeText(this, "onClick() called", Toast.LENGTH_LONG).show()
            // AlertDialog.Builder(this).setTitle().setMessage().setPositiveButton("OK"){ dialog, id ->
            // Toast.makeText(...)
            // .setNavigationButton("Cancel"}

            // Save user credentials to file
            val inputtedUsername = username.text.toString()
            val inputtedPassword = password.text.toString() //getText() ---- username.setText()
            //can encrypt here, but wont for this example ***


            firebaseAuth
                .signInWithEmailAndPassword(inputtedUsername, inputtedPassword)
                .addOnCompleteListener {task: Task<AuthResult> ->
                    if (task.isSuccessful){
                        val currentUser: FirebaseUser = firebaseAuth.currentUser!!
                        val email = currentUser.email

                        // Firebase analytics
                        firebaseAnalytics.logEvent("login_success", null)

                        // Set Preferences
                        preferences
                            .edit()
                            .putString("username", inputtedUsername)
                            .putString("password", inputtedPassword)
                            .apply()

                        // Go to SearchArticles Activity now
                        val intent: Intent = Intent(this, SearchArticles::class.java) //go from here to TweetsActivity class
                        startActivity(intent) //execute intent

                        Toast.makeText(this, "Logged in successfully as $email", Toast.LENGTH_SHORT).show()
                    } else {
                        val exception: Exception = task.exception!!
                        val errorType = if (exception is FirebaseAuthInvalidCredentialsException){
                            "invalid_credentials"
                        } else {
                            "unknown_error"
                        }

                        // Firebase Analytics
                        val bundle = Bundle()
                        bundle.putString("error_type", errorType)
                        firebaseAnalytics.logEvent("login_failed", bundle)

                        when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> Toast.makeText(
                                this,
                                "Bad Credentials!",
                                Toast.LENGTH_SHORT
                            ).show()
                            is FirebaseAuthUserCollisionException -> Toast.makeText(
                                this,
                                "Already a user!",
                                Toast.LENGTH_SHORT
                            ).show()
                            else -> Toast.makeText(
                                this,
                                "Failed: $exception!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }

        // Go to register activity
        register.setOnClickListener {
            Log.d("MainActivity", "register button clicked")

            //send intent to move to next screen
            val intent: Intent = Intent(this, SignUpActivity::class.java) //go from here to TweetsActivity class
            startActivity(intent) //execute intent
        }

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


    private val textWatcher = object: TextWatcher {
        //can take out ? nullable parameter values
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            val inputtedUsername = username.text.toString()
            val inputtedPassword = password.text.toString() //getText() ---- username.setText()

            val enable: Boolean = inputtedUsername.trim().isNotEmpty() && inputtedPassword.trim().isNotEmpty()

            login.isEnabled = enable // or login.setEnabled(enable)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername = username.text.toString()
            val inputtedPassword = password.text.toString() //getText() ---- username.setText()

            val enable: Boolean = inputtedUsername.trim().isNotEmpty() && inputtedPassword.trim().isNotEmpty()

            login.isEnabled = enable // or login.setEnabled(enable)
        }


    }

    // Get context from CurrentArticles
    companion object {
        fun getContext(): Context {
            return this.getContext()
        }
    }
}

