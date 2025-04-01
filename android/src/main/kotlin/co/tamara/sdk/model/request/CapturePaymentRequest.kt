package co.tamara.sdk.model.request

import android.os.Parcelable
import co.tamara.sdk.model.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class CapturePaymentRequest(
    @SerializedName("order_id") var orderId: String? = null,
    @SerializedName("billing_address") var billingAddress: Address? = null,
    @SerializedName("discount_amount") var discountAmount: Amount? = null,
    var items: ArrayList<Item> = arrayListOf(),
    @SerializedName("shipping_address") var shippingAddress: Address? = null,
    @SerializedName("shipping_amount") var shippingAmount: Amount? = null,
    @SerializedName("tax_amount") var taxAmount: Amount? = null,
    @SerializedName("total_amount") var totalAmount: Amount? = null,
    @SerializedName("shipping_info") var shippingInfo: ShippingInfo? = null
): Parcelable