package com.example.employee.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.employee.R
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var tvCount: TextView
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)
        tvCount = findViewById(R.id.tvCount)
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        //navigate to add sellers page
        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        //navigate to all sellers page
        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

        //count all sellers
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                tvCount.text = "Number of sellers: $count"
            }

            override fun onCancelled(error: DatabaseError) {
                //handle error
            }
        })
    }
}