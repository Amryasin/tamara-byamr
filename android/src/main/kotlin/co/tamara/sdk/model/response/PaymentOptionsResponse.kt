package co.tamara.sdk.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentOptionsResponse(
    @SerializedName("has_available_payment_options") var hasAvailablePaymentOptions: Boolean? = false,
    @SerializedName("single_checkout_enabled") var singleCheckoutEnabled: Boolean? = false,
    @SerializedName("available_payment_labels") var availablePaymentLabels: ArrayList<AvailablePayment> = arrayListOf()
) : Parcelable