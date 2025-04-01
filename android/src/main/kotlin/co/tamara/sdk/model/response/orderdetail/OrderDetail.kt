package co.tamara.sdk.model.response.orderdetail

import android.os.Parcelable
import co.tamara.sdk.model.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetail (
    @SerializedName("billing_address") var billingAddress: AddressDetail? = null,
    var consumer: ConsumerDetail? = null,
    @SerializedName("country_code") var countryCode: String? = "SA", // SA
    var description: String? = "This is description", // string
    var status: String? = "This is description", // string
    @SerializedName("discount_amount") var discountAmount: DiscountDetail? = null,
    var items: ArrayList<ItemDetail> = arrayListOf(),
    @SerializedName("order_reference_id") var orderReferenceId: String = "", // 123456
    @SerializedName("order_id") var orderId: String? = "", // 123456
    @SerializedName("order_number") var orderNumber: String? = "", // 123456
    @SerializedName("payment_type") var paymentType: String = "PAY_BY_LATER", // PAY_BY_LATER
    @SerializedName("shipping_address") var shippingAddress: AddressDetail? = null,
    @SerializedName("shipping_amount") var shippingAmount: AmountDetail? = null,
    @SerializedName("tax_amount") var taxAmount: AmountDetail? = null,
    @SerializedName("total_amount") var totalAmount: AmountDetail? = null,
    @SerializedName("captured_amount") var capturedAmount: AmountDetail? = null,
    @SerializedName("refunded_amount") var refundedAmount: AmountDetail? = null,
    @SerializedName("canceled_amount") var canceledAmount: AmountDetail? = null,
    @SerializedName("paid_amount") var paidAmount: AmountDetail? = null,
    @SerializedName("wallet_prepaid_amount") var walletPrepaidAmount: AmountDetail? = null,
    var platform: String = "Android", //
    @SerializedName("settlement_status") var settlementStatus: String? = null,
    @SerializedName("settlement_date") var settlementDate: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    var instalments: Int? = null
): Parcelable