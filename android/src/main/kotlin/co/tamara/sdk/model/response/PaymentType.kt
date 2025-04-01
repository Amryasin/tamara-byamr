package co.tamara.sdk.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class PaymentType(
    var name: String = "",
    var description: String = "",
    var description_ar: String = ""
) : Parcelable