package com.example.f2c.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.f2c.R
import com.example.f2c.model.ReviewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddReviewActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etReview: EditText
    private lateinit var addReview: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        etEmail = findViewById(R.id.editEmail)
        etReview = findViewById(R.id.editReview)
        addReview = findViewById(R.id.submitReview)

        dbRef = FirebaseDatabase.getInstance().getReference("Reviews")

        addReview.setOnClickListener {
            saveReviewData()
        }

        var back = findViewById<Button>(R.id.btnCancel)
        back.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveReviewData() {

        //getting values
        val cusEmail = etEmail.text.toString()
        val cusReview = etReview.text.toString()

        if (cusEmail.isEmpty()) {
            etEmail.error = "Please enter email"
        }
        if (cusReview.isEmpty()) {
            etReview.error = "Please enter review"
        }

        val reviewId = dbRef.push().key!!

        val review = ReviewModel(reviewId, cusEmail, cusReview)

        dbRef.child(reviewId).setValue(review)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etEmail.text.clear()
                etReview.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}