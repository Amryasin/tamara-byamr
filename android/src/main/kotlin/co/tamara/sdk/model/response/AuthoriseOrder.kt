package co.tamara.sdk.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthoriseOrder (
    @SerializedName("order_id") var orderId: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("order_expiry_time") var orderExpiryTime: String = "",
    @SerializedName("payment_type") var paymentType: String = "",
    @SerializedName("auto_captured") var autoCaptured: String = ""
) : Parcelable