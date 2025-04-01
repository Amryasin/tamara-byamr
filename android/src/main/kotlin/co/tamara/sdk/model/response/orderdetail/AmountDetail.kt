package co.tamara.sdk.model.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AmountDetail(
    var amount: Double = 0.0, // 50.00
    var currency: String? = "" // SAR
): Parcelable