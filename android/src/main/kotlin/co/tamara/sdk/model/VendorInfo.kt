package co.tamara.sdk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VendorInfo(
    @SerializedName("vendor_amount") var vendorAmount: Int? = null,
    @SerializedName("merchant_settlement_amount") var merchantSettlementAmount: Int? = null,
    @SerializedName("vendor_reference_code") var vendorReferenceCode: String? = null
) : Parcelable