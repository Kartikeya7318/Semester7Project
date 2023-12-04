package com.example.grocery

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.adapter.CartAdapter
import com.example.grocery.adapter.CounterMapChangeListener
import com.example.grocery.model.DummyDataItem
import java.io.Serializable

class CartActivity : AppCompatActivity(), CounterMapChangeListener {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var tvTotalBill: TextView

    private lateinit var selectedProducts: ArrayList<DummyDataItem>
    private lateinit var counterMap: MutableMap<Int, Int>

    private lateinit var btnPlaceOrder: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbar = findViewById(R.id.toolbar)
        recyclerViewCart = findViewById(R.id.recyclerViewCart)
        tvTotalBill = findViewById(R.id.tvTotalBill)
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder)
        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve data from intent
        selectedProducts = intent.getSerializableExtra("data") as ArrayList<DummyDataItem>
        counterMap = intent.getSerializableExtra("counterMap") as MutableMap<Int, Int>
        val filteredSelectedProducts = selectedProducts.filter { product ->
            val count = counterMap[product.id] ?: 0
            count > 0
        } as ArrayList<DummyDataItem>

        // Set up RecyclerView
        val adapter = CartAdapter(filteredSelectedProducts, counterMap,this)
        recyclerViewCart.layoutManager = LinearLayoutManager(this)
        recyclerViewCart.adapter = adapter

        // Calculate and display total bill
        val totalBill = calculateTotalBill(filteredSelectedProducts, counterMap)
        tvTotalBill.text = "₹$totalBill"

        btnPlaceOrder.setOnClickListener {
            Toast.makeText(this,"Order Placed Success",Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateTotalBill(
        selectedProducts: List<DummyDataItem>,
        counterMap: Map<Int, Int>
    ): Double {
        var totalBill = 0.0
        for (product in selectedProducts) {
            val count = counterMap[product.id] ?: 0
            totalBill += count * product.price
        }
        return totalBill
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCounterMapChange(counterMap: Map<Int, Int>) {
        Log.i("MainActivity", "onCounterMapChange: ")
        val totalBill = calculateTotalBill(selectedProducts, counterMap)
        tvTotalBill.text = "₹$totalBill"
    }
}
