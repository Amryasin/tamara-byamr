package co.tamara.sdk.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RefundsResponse (
    @SerializedName("order_id") var orderId: String? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("refund_id") var refundId: String? = null,
    @SerializedName("capture_id") var captureId: String? = null
): Parcelable