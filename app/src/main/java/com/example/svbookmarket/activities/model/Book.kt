package com.example.svbookmarket.activities.model

import android.os.Parcel
import android.os.Parcelable

data class Book(
    var id: String? = "",
    var Image: String? = "",
    var Name: String? = "",
    var Kind: String? ="",
    var Description: String? = "",
    var Saler: String? = "",
    var SalerName: String?=""):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(Image)
        parcel.writeString(Name)
        parcel.writeString(Kind)
        parcel.writeString(Description)
        parcel.writeString(Saler)
        parcel.writeString(SalerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
