package com.example.mycart.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycart.R
import com.example.mycart.adapter.CartAdapter
import com.example.mycart.model.CartModel
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var cartList: ArrayList<CartModel>
    private lateinit var dbRef: DatabaseReference

    //getting all the items in the cart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        cartRecyclerView = findViewById(R.id.rvCart)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        cartList = arrayListOf<CartModel>()

        getCartData()

    }

    private fun getCartData() {

        cartRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Cart")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()
                if (snapshot.exists()){
                    for (cartSnap in snapshot.children){
                        val cartData = cartSnap.getValue(CartModel::class.java)
                        cartList.add(cartData!!)
                    }
                    val mAdapter = CartAdapter(cartList)
                    cartRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CartAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, CartDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("cartId", cartList[position].cartId)
                            intent.putExtra("cartName", cartList[position].cartName)
                            intent.putExtra("cartQuantity", cartList[position].cartQuantity)
                            intent.putExtra("cartDelivery", cartList[position].cartDelivery)
                            startActivity(intent)
                        }

                    })

                    cartRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}