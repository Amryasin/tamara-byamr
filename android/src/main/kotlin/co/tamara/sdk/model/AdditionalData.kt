package co.tamara.sdk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdditionalData(
    @SerializedName("delivery_method") var deliveryMethod: String? = null,
    @SerializedName("pickup_store") var pickupStore: String? = null,
    @SerializedName("store_code") var storeCode: String? = null,
    @SerializedName("vendor_info") var vendorInfo: ArrayList<VendorInfo> = arrayListOf()
) : Parcelable