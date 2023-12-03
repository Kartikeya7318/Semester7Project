package com.example.grocery.model

// DummyDataItem.kt
data class DummyDataItem(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String,
    val quantity: Int,
    val price: Int,
    val description: String,
    val rating: Float,
    val weight: String,
    val manufacturer: String,
    val expiryDate: String
) {
    constructor() : this(0, "", "", "", 0, 0, "", 0.0f, "", "", "")

}