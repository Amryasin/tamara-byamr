package co.tamara.sdk.model.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiscountDetail(
    var amount: AmountDetail,
    var name: String = "" // Christmas 2020
): Parcelable