package co.tamara.sdk.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderReference (
    var message: String = ""
): Parcelable