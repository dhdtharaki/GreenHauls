package com.example.f2c.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.f2c.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        var addReview = findViewById<Button>(R.id.addReview)
        addReview.setOnClickListener {
            var intent = Intent(this, AddReviewActivity::class.java)
            startActivity(intent)
        }

        var viewReview  = findViewById<Button>(R.id.viewReview)
        viewReview.setOnClickListener {
            var intent = Intent(this, DisplayReviewsActivity::class.java)
            startActivity(intent)
        }
    }
}