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
import androidx.core.view.isVisible
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import java.lang.Exception
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var username: EditText

    private lateinit var password: EditText

    private lateinit var passwordConfirm: EditText

    private lateinit var signUp: Button

    private lateinit var progressBar: ProgressBar

    object Constants {
        val INTENT_KEY_LOCATION = "LOCATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        Log.d("LoginActivity", "onCreate() called")

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAuth = FirebaseAuth.getInstance()

        val preferences: SharedPreferences = getSharedPreferences(
            "android-tweets",
            Context.MODE_PRIVATE
        )

        //initialize variables
        username = findViewById(R.id.usernameSignUp)
        password = findViewById(R.id.passwordSignUp)
        passwordConfirm = findViewById(R.id.passwordConfirmSignUp)
        signUp = findViewById(R.id.register)
        progressBar = findViewById(R.id.progressBarSignUp)

        progressBar.isVisible = false


        //listeners
        signUp.isEnabled = false // set to false initially
        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        passwordConfirm.addTextChangedListener(textWatcher)


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


        // Sign Up Listener
        signUp.setOnClickListener {
            // Get user credentials
            val inputtedUsername = username.text.toString()
            val inputtedPassword = password.text.toString()

            progressBar.isVisible = true

            firebaseAuth
                .createUserWithEmailAndPassword(inputtedUsername, inputtedPassword)
                .addOnCompleteListener {task: Task<AuthResult> ->
                    if (task.isSuccessful){
                        val currentUser: FirebaseUser = firebaseAuth.currentUser!!
                        val email = currentUser.email

                        Toast.makeText(this, "Registered successfully as $email", Toast.LENGTH_SHORT).show()
                    } else {
                        val exception: Exception = task.exception!!
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


            // Go to login page now
            val intent: Intent = Intent(this, LoginActivity::class.java) //go from here to TweetsActivity class
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
            val inputtedConfirmPassword = passwordConfirm.text.toString()

            val enable: Boolean = inputtedUsername.trim().isNotEmpty() && inputtedPassword.trim().isNotEmpty() && inputtedConfirmPassword.trim().isNotEmpty()

            signUp.isEnabled = enable // or login.setEnabled(enable)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername = username.text.toString()
            val inputtedPassword = password.text.toString() //getText() ---- username.setText()
            val inputtedConfirmPassword = passwordConfirm.text.toString()

            val enable: Boolean = inputtedUsername.trim().isNotEmpty() && inputtedPassword.trim().isNotEmpty() && inputtedConfirmPassword.trim().isNotEmpty()

            signUp.isEnabled = enable // or login.setEnabled(enable)
        }


    }
}

