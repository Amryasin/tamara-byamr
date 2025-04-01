package co.tamara.sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class Amount(
    var amount: Double = 0.0, // 50.00
    var currency: String? = "" // SAR
): Parcelable