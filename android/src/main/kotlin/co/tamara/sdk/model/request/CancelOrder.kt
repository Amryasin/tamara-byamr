package co.tamara.sdk.model.request

import android.os.Parcelable
import co.tamara.sdk.model.Amount
import co.tamara.sdk.model.Item
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class CancelOrder (
    @SerializedName("discount_amount") var discountAmount: Amount? = null,
    var items: ArrayList<Item> = arrayListOf(),
    @SerializedName("shipping_amount") var shippingAmount: Amount? = null,
    @SerializedName("tax_amount") var taxAmount: Amount? = null,
    @SerializedName("total_amount") var totalAmount: Amount? = null
): Parcelable