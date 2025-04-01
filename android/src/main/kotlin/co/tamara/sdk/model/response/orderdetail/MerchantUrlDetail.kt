package co.tamara.sdk.model.response.orderdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MerchantUrlDetail(
    var notification: String = "tamara://checkout/notification", // https://example.com/payments/tamarapay
    var cancel: String = "tamara://checkout/cancel", // https://example.com/checkout/cancel
    var failure: String = "tamara://checkout/failure", // https://example.com/checkout/failure
    var success: String = "tamara://checkout/success" // https://example.com/checkout/success
): Parcelable