package com.example.grocery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.adapter.RecyclerViewAdapter
import com.example.grocery.model.DummyDataItem
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    // Initialize Firestore
    private lateinit var firestore :FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        firestore= FirebaseFirestore.getInstance()
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        var dummyData: ArrayList<DummyDataItem> = ArrayList();
        dummyData.add(DummyDataItem(
            id = 1,
            title = "Product 1",
            category = "Vegetables",
            imageUrl = "https://example.com/product1.jpg",
            quantity = 10,
            price = 5,
            description = "Fresh and organic vegetables",
            rating = 4.5f,
            weight = "500g",
            manufacturer = "Farmers Co.",
            expiryDate = "2023-12-31"
        ))
        dummyData.add(DummyDataItem(
            id = 2,
            title = "Product 2",
            category = "Fruits",
            imageUrl = "https://example.com/product2.jpg",
            quantity = 8,
            price = 8,
            description = "Assorted fruits pack",
            rating = 4.2f,
            weight = "1kg",
            manufacturer = "Fruit Haven",
            expiryDate = "2023-12-30"
        ))
        dummyData.add(DummyDataItem(
            id = 3,
            title = "Product 3",
            category = "Dairy",
            imageUrl = "https://example.com/product3.jpg",
            quantity = 15,
            price = 3,
            description = "Fresh milk",
            rating = 4.0f,
            weight = "1L",
            manufacturer = "Dairy Delight",
            expiryDate = "2023-12-29"
        ))
        // Dummy data



        val adapter = RecyclerViewAdapter(this, dummyData)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        fetchDataFromFirestore {products ->
            adapter.updateData(products)

            // Notify the adapter of the changes
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"Data fetch success",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                // Handle profile click
                // Replace with navigation to ProfileFragment or ProfileActivity
            }
            R.id.nav_categories -> {
                // Handle categories click
                // Replace with navigation to CategoriesFragment or CategoriesActivity
            }
            R.id.nav_cart -> {
                // Handle cart click
                // Replace with navigation to CartFragment or CartActivity
            }
            R.id.nav_order -> {
                // Handle order click
                // Replace with navigation to OrderFragment or OrderActivity
            }
            R.id.nav_logout -> {
                logoutUser()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun logoutUser() {
        // Use FirebaseAuth to sign out the user
        FirebaseAuth.getInstance().signOut()

        // After logout, navigate to the login screen or perform other actions
        startLoginActivity()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun fetchDataFromFirestore(onSuccess: (List<DummyDataItem>) -> Unit) {
        val productsCollection = firestore.collection("Products")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = productsCollection.get().await()

                val products = mutableListOf<DummyDataItem>()
                Log.i("MainActivity", "fetchDataFromFirestore: "+products.size)
                for (document in querySnapshot.documents) {
                    val item = document.toObject(DummyDataItem::class.java)
                    item?.let { products.add(item) }
                }
                val sortedProducts = products.sortedBy { it.id }
                // Notify the UI thread with the fetched data
                launch(Dispatchers.Main) {
                    onSuccess(sortedProducts)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "fetchDataFromFirestore: ", e)
                // Handle error, show a toast, log, etc.
            }
        }
    }
}
