package co.tamara.sdk.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class OrderReference (
    @SerializedName("order_reference_id") var orderReferenceId: String? = null
): Parcelable