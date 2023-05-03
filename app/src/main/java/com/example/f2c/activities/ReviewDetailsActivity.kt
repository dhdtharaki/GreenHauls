package com.example.f2c.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.f2c.R
import com.example.f2c.model.ReviewModel
import com.google.firebase.database.FirebaseDatabase

class ReviewDetailsActivity : AppCompatActivity() {
    private lateinit var tvReviewId: TextView
    private lateinit var tvCusEmail: TextView
    private lateinit var tvCusReview: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateReviewDialog(
                intent.getStringExtra("reviewId").toString(),
                intent.getStringExtra("cusEmail").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("reviewId").toString()
            )
        }
        var back = findViewById<Button>(R.id.btnBack)
        back.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        tvReviewId = findViewById(R.id.tvReviewId)
        tvCusEmail = findViewById(R.id.tvCusEmail)
        tvCusReview = findViewById(R.id.tvCusReview)

        btnUpdate = findViewById(R.id.btnUpdateReview)
        btnDelete = findViewById(R.id.btnDeleteReview)
    }

    private fun setValuesToViews() {
        tvReviewId.text = intent.getStringExtra("reviewId")
        tvCusEmail.text = intent.getStringExtra("cusEmail")
        tvCusReview.text = intent.getStringExtra("cusReview")
    }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Reviews").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Review data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DisplayReviewsActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateReviewDialog(
        reviewId: String,
        cusEmail: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_review_dialog, null)

        mDialog.setView(mDialogView)

        val etCusEmail = mDialogView.findViewById<EditText>(R.id.etCusEmail)
        val etCusReview = mDialogView.findViewById<EditText>(R.id.etCusReview)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateReviewData)

        etCusEmail.setText(intent.getStringExtra("cusEmail").toString())
        etCusReview.setText(intent.getStringExtra("cusReview").toString())

        mDialog.setTitle("Updating $cusEmail Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateReviewData(
                reviewId,
                etCusEmail.text.toString(),
                etCusReview.text.toString(),
            )

            Toast.makeText(applicationContext, "Review Data Updated", Toast.LENGTH_LONG).show()

            //setting updated data to our text views
            tvCusEmail.text = etCusEmail.text.toString()
            tvCusReview.text = etCusReview.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateReviewData(
        id: String,
        email: String,
        review: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Reviews").child(id)
        val reviewInfo = ReviewModel(id, email, review)
        dbRef.setValue(reviewInfo)
    }

}