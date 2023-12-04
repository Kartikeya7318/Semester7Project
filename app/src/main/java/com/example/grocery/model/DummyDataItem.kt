package com.example.grocery.model

import android.os.Parcel
import android.os.Parcelable

data class DummyDataItem(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String,
    val quantity: String,
    val price: Int,
    val description: String,
    val rating: Float,
    val weight: String,
    val manufacturer: String,
    val expiryDate: String
) : Parcelable {


    constructor() : this(0, "", "", "", "0", 0, "", 0.0f, "", "", "")
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeString(imageUrl)
        parcel.writeString(quantity)
        parcel.writeInt(price)
        parcel.writeString(description)
        parcel.writeFloat(rating)
        parcel.writeString(weight)
        parcel.writeString(manufacturer)
        parcel.writeString(expiryDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DummyDataItem> {
        override fun createFromParcel(parcel: Parcel): DummyDataItem {
            return DummyDataItem(parcel)
        }

        override fun newArray(size: Int): Array<DummyDataItem?> {
            return arrayOfNulls(size)
        }
    }
}
