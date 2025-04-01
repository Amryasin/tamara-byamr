package co.tamara.sdk.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class CheckoutSession(
    var checkout_url: String = "", // https://tamarapay.com/checkout/0a17d40a-6180-4f4b-ba7c-498ae79e30dc
    var order_id: String = "" // 0a17d40a-6180-4f4b-ba7c-498ae79e30dc
): Parcelable