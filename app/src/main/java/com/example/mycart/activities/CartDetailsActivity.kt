package com.example.mycart.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mycart.model.CartModel
import com.example.mycart.R
import com.google.firebase.database.FirebaseDatabase

class CartDetailsActivity : AppCompatActivity() {

    private lateinit var tvCartId: TextView
    private lateinit var tvCartName: TextView
    private lateinit var tvCartQuantity: TextView
    private lateinit var tvCartDelivery: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_details)

        initView()
        setValuesToViews()

        //update function call
        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cartId").toString(),
                intent.getStringExtra("cartName").toString()
            )
        }

        //delete function call
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cartId").toString()
            )
        }

    }

    private fun initView() {
        tvCartId = findViewById(R.id.tvCartId)
        tvCartName = findViewById(R.id.tvCartName)
        tvCartQuantity = findViewById(R.id.tvCartQuantity)
        tvCartDelivery = findViewById(R.id.tvCartDelivery)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }


   //default get the values from database
    private fun setValuesToViews() {
        tvCartId.text = intent.getStringExtra("cartId")
        tvCartName.text = intent.getStringExtra("cartName")
        tvCartQuantity.text = intent.getStringExtra("cartQuantity")
        tvCartDelivery.text = intent.getStringExtra("cartDelivery")

    }

    //delete recode from the database
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(id)
        val mTask = dbRef.removeValue()

        //getting item delete message
        mTask.addOnSuccessListener {
            Toast.makeText(this, "Item data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }


    //getting the item report by id
    private fun openUpdateDialog(
        cartId: String,
        cartName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etCartName = mDialogView.findViewById<EditText>(R.id.etCartName)
        val etCartQuantity = mDialogView.findViewById<EditText>(R.id.etCartQuantity)
        val  etCartDelivery = mDialogView.findViewById<EditText>(R.id.etCartDelivery)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etCartName.setText(intent.getStringExtra("cartName").toString())
        etCartQuantity.setText(intent.getStringExtra("cartQuantity").toString())
        etCartDelivery.setText(intent.getStringExtra("cartDelivery").toString())

        mDialog.setTitle("Updating $cartName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateCartData(
                cartId,
                etCartName.text.toString(),
                etCartQuantity.text.toString(),
                etCartDelivery.text.toString()
            )

            //getting updated message
            Toast.makeText(applicationContext, "Item Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvCartName.text = etCartName.text.toString()
            tvCartQuantity.text = etCartQuantity.text.toString()
            tvCartDelivery.text = etCartDelivery.text.toString()

            alertDialog.dismiss()
        }
    }

    //update recode from the database
    private fun updateCartData(
        id: String,
        name: String,
        quantity: String,
        delivery: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(id)
        val cartInfo = CartModel(id, name, quantity, delivery)
        dbRef.setValue(cartInfo)
    }

}