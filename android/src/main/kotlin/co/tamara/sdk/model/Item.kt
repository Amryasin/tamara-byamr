package co.tamara.sdk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class Item(
    var name: String = "", // Lego City 8601
    @SerializedName("reference_id") var referenceId: String = "", // 123456
    var sku: String = "", // SA-12436
    var type: String = "", // SA-12436
    @SerializedName("unit_price") var unitPrice: Amount? = null,
    var quantity: Int = 0, // 1
    @SerializedName("discount_amount") var discountAmount: Amount? = null,
    @SerializedName("tax_amount") var taxAmount: Amount? = null,
    @SerializedName("total_amount") var totalAmount:Amount? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("item_url") var itemUrl: String? = null
): Parcelable