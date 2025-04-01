package co.tamara.sdk.model.response.orderdetail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConsumerDetail(
    @SerializedName("first_name") var firstName: String? = "", // Mona
    @SerializedName("last_name") var lastName: String? = "", // Lisa
    @SerializedName("phone_number") var phoneNumber: String? = "", // 502223333
    var email: String = "", // user@example.com
    @SerializedName("is_first_order") var isFirstOrder: Boolean? = false, // true
    @SerializedName("date_of_birth") var dateOfBirth: String? = "", // string
    @SerializedName("national_id") var nationalId: String? = "" // 123456
): Parcelable