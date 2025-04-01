package co.tamara.sdk.model

import android.app.Activity
import android.os.Parcelable
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
internal data class Order(
    @SerializedName("billing_address") var billingAddress: Address? = null,
    var consumer: Consumer? = null,
    @SerializedName("country_code") var countryCode: String = "SA", // SA
    var description: String = "This is description", // string
    var discount: Discount? = null,
    var items: ArrayList<Item> = arrayListOf(),
    var locale: String = "en-US", // en-US
    @SerializedName("merchant_url") var merchantUrl: MerchantUrl = MerchantUrl(),
    @SerializedName("order_reference_id") var orderReferenceId: String = "", // 123456
    @SerializedName("payment_type") var paymentType: String = "PAY_BY_INSTALMENTS", // PAY_BY_LATER
    @SerializedName("shipping_address") var shippingAddress: Address? = null,
    @SerializedName("shipping_amount") var shippingAmount: Amount? = null,
    @SerializedName("tax_amount") var taxAmount: Amount? = null,
    @SerializedName("total_amount") var totalAmount: Amount? = null,
    var platform: String = "Android", //
    @SerializedName("is_mobile") var isMobile: Boolean = true,
    @SerializedName("instalments") var instalments: Int? = null,
    @SerializedName("order_number") var orderNumber: String? = null,
    @SerializedName("expires_in_minutes") var expiresInMinutes: Int? = null,
    @SerializedName("risk_assessment") var riskAssessment: @RawValue MutableMap<String, Any?> = mutableMapOf(),
    @SerializedName("additional_data") var additionalData: @RawValue MutableMap<String, Any?> = mutableMapOf()
) : Parcelable

internal fun Order.updateAdditionalDataFromJson(jsonString: String) {
    try {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        jsonObject?.entrySet()?.forEach { entry ->
            val fieldName = entry.key
            val jsonElement = entry.value
            additionalData[fieldName] = gson.fromJson(jsonElement, Any::class.java)
        }
    } catch (e: Exception) {
        e.message
    }
}

internal fun Order.updateRiskAssessmentFromJson(jsonString: String, activity: Activity) : Boolean {
    try {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        jsonObject?.entrySet()?.forEach { entry ->
            val fieldName = entry.key
            val jsonElement = entry.value
            riskAssessment[fieldName] = gson.fromJson(jsonElement, Any::class.java)
        }
        return true
    } catch (e: Exception) {
        Toast.makeText(
            activity, e.message,
            Toast.LENGTH_LONG
        ).show()
        return false
    }
}