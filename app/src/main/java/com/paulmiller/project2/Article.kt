package com.paulmiller.project2

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Article(
    //name, price, address, rating
    val web_url: String,
    val snippet: String,
    val lead_paragraph: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(web_url)
        parcel.writeString(snippet)
        parcel.writeString(lead_paragraph)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}