package co.tamara.sdk.model.response.orderdetail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressDetail(
    @SerializedName("first_name") var firstName: String = "",
    @SerializedName("last_name") var lastName: String = "",
    @SerializedName("phone_number") var phoneNumber: String = "",
    var line1: String = "",
    var line2: String = "",
    @SerializedName("country_code") var countryCode: String = "",
    var region: String = "",
    var city: String = ""
): Parcelable