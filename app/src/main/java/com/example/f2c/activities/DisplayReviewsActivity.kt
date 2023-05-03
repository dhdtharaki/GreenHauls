package com.example.f2c.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f2c.R
import com.example.f2c.adapter.ReviewAdapter
import com.example.f2c.model.ReviewModel
import com.google.firebase.database.*

class DisplayReviewsActivity : AppCompatActivity() {
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var reviewList: ArrayList<ReviewModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_reviews)

        reviewRecyclerView = findViewById(R.id.rvReview)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        reviewList = arrayListOf<ReviewModel>()

        getReviewsData()

    }

    private fun getReviewsData() {

        reviewRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Reviews")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reviewList.clear()
                if (snapshot.exists()){
                    for (reviewSnap in snapshot.children){
                        val reviewData = reviewSnap.getValue(ReviewModel::class.java)
                        reviewList.add(reviewData!!)
                    }
                    val mAdapter = ReviewAdapter(reviewList)
                    reviewRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ReviewAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@DisplayReviewsActivity, ReviewDetailsActivity::class.java)

                            intent.putExtra("reviewId", reviewList[position].reviewID)
                            intent.putExtra("cusEmail", reviewList[position].cusEmail)
                            intent.putExtra("cusReview", reviewList[position].cusReview)
                            startActivity(intent)
                        }

                    })

                    reviewRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}