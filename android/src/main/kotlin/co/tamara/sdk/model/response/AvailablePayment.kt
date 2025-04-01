package co.tamara.sdk.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AvailablePayment (
    var payment_type: String? = null,
    var description_en: String? = null,
    var description_ar: String? = null,
    var instalment: Int = 0,
): Parcelable