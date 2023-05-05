package com.example.mycart.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mycart.R


class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button

    // declare a variable to keep track of the total number of items in the cart
    private var totalItemsInCart = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)

        //navigate to the add items page
        btnInsertData.setOnClickListener {

            // increment the total number of items in the cart each time an item is added
            totalItemsInCart++

            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)

        }

        //navigate to the my cart
        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
            // display the total number of items in the cart as a message in the "View Your Cart" button
            btnFetchData.text = "View Your Cart (${totalItemsInCart} items)"
        }

    }
}