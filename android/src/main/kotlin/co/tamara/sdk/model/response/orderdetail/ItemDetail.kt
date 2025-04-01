package co.tamara.sdk.model.response.orderdetail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemDetail(
    var name: String = "", // Lego City 8601
    @SerializedName("reference_id") var referenceId: String = "", // 123456
    var sku: String = "", // SA-12436
    @SerializedName("unit_price") var unitPrice: AmountDetail? = null,
    var quantity: Int = 0, // 1
    @SerializedName("discount_amount") var discountAmount: AmountDetail? = null,
    @SerializedName("tax_amount") var taxAmount: AmountDetail? = null,
    @SerializedName("total_amount") var totalAmount:AmountDetail? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("item_url") var itemUrl: String? = null
): Parcelable