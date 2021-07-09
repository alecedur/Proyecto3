package com.example.proyecto3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class HighScores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        val buttonMainMenu = findViewById<Button>(R.id.Menu_Principal)

        val receiver_msg = findViewById<TextView>(R.id.received_value_id)
        val receiver_msg_2 = findViewById<TextView>(R.id.received_value_2_id)
        val receiver_msg_3 = findViewById<TextView>(R.id.received_value_3_id)
        val receiver_msg_4 = findViewById<TextView>(R.id.received_value_4_id)
        val receiver_msg_5 = findViewById<TextView>(R.id.received_value_5_id)

        val received_score_1 = findViewById<TextView>(R.id.received_score_1_id)
        val received_score_2 = findViewById<TextView>(R.id.received_score_2_id)
        val received_score_3 = findViewById<TextView>(R.id.received_score_3_id)
        val received_score_4 = findViewById<TextView>(R.id.received_score_4_id)
        val received_score_5 = findViewById<TextView>(R.id.received_score_5_id)

        // create the get Intent object
        val intent = intent

        // receive the value by getStringExtra() method
        // and key must be same which is send by first activity
        val str = intent.getStringExtra("message_key")



        //display name sent
        receiver_msg.setText(str)

        //boton splash activity
        buttonMainMenu.setOnClickListener(View.OnClickListener {
            // Start your app main activity

            startActivity(Intent(this,SplashActivity::class.java))

            // close this activity
            finish()
        })


    }
}