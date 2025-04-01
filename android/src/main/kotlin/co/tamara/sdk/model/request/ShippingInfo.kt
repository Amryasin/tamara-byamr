package co.tamara.sdk.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class ShippingInfo (
    @SerializedName("shipped_at") var shippedAt: String? = "",
    @SerializedName("shipping_company") var shippingCompany: String? = "",
    @SerializedName("tracking_number") var trackingNumber: String? = "",
    @SerializedName("tracking_url") var trackingUrl: String? = ""
): Parcelable