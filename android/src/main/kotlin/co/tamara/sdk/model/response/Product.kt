package co.tamara.sdk.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    var script: String = "",
    var url: String = ""
) : Parcelable