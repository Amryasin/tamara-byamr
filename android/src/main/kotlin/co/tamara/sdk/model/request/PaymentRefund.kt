package co.tamara.sdk.model.request

import android.os.Parcelable
import co.tamara.sdk.model.Amount
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class PaymentRefund (
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("total_amount") var totalAmount: Amount? = null
): Parcelable