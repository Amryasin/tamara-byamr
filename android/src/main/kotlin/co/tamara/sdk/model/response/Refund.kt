package co.tamara.sdk.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Refund (
    @SerializedName("capture_id") var captureId: String? = null,
    @SerializedName("refund_id") var refundId: String? = null
): Parcelable