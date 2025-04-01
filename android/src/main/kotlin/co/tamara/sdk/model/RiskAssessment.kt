package co.tamara.sdk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RiskAssessment(
    @SerializedName("customer_age") var customerAge: Int? = null,
    @SerializedName("customer_dob") var customerDob: String? = null,
    @SerializedName("customer_gender") var customerGender: String? = null,
    @SerializedName("customer_nationality") var customerNationality: String? = null,
    @SerializedName("is_premium_customer") var isPremiumCustomer: Boolean? = null,
    @SerializedName("is_existing_customer") var isExistingCustomer: Boolean? = null,
    @SerializedName("is_guest_user") var isGuestUser: Boolean? = null,
    @SerializedName("account_creation_date") var accountCreationDate: String? = null,
    @SerializedName("platform_account_creation_date") var platformAccountCreationDate: String? = null,
    @SerializedName("date_of_first_transaction") var dateOfFirstTransaction: String? = null,
    @SerializedName("is_card_on_file") var isCardOnFile: Boolean? = null,
    @SerializedName("is_COD_customer") var isCODCustomer: Boolean? = null,
    @SerializedName("has_delivered_order") var hasDeliveredOrder: Boolean? = null,
    @SerializedName("is_phone_verified") var isPhoneVerified: Boolean? = null,
    @SerializedName("is_fraudulent_customer") var isFraudulentCustomer: Boolean? = null,
    @SerializedName("total_ltv") var totalLtv: Double? = null,
    @SerializedName("total_order_count") var totalOrderCount: Int? = null,
    @SerializedName("order_amount_last3months") var orderAmountLast3months: Double? = null,
    @SerializedName("order_count_last3months") var orderCountLast3months: Int? = null,
    @SerializedName("last_order_date") var lastOrderDate: String? = null,
    @SerializedName("last_order_amount") var lastOrderAmount: Double? = null,
    @SerializedName("reward_program_enrolled") var rewardProgramEnrolled: Boolean? = null,
    @SerializedName("reward_program_points") var rewardProgramPoints: Int? = null
) : Parcelable