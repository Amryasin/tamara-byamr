package co.tamara.sdk.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CapturePayment(
    @SerializedName("capture_id") var captureId: String = "",
    @SerializedName("order_id") var orderId: String = ""
) : Parcelable