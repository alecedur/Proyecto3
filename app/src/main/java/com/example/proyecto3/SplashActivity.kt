package com.example.proyecto3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button

class SplashActivity : AppCompatActivity() {

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val buttonInit = findViewById<Button>(R.id.Inicio)
        val buttonHS = findViewById<Button>(R.id.Highscores)

        /*Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this,MainActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)*/

        buttonInit.setOnClickListener(View.OnClickListener {
            // Start your app main activity

            startActivity(Intent(this,MainActivity::class.java))

            // close this activity
            finish()
        })

        buttonHS.setOnClickListener(View.OnClickListener {
            // Start your app main activity

            //startActivity(Intent(this,HighScores2::class.java))

            // close this activity
            finish()
        })

    }

}