package co.tamara.sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class Discount(
    var amount: Amount,
    var name: String = "" // Christmas 2020
): Parcelable