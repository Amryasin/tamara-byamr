package co.tamara.sdk.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class WidgetProperties (
    @SerializedName("language") var language: String = "",
    @SerializedName("country") var country: String = "",
    @SerializedName("publicKey") var publicKey: String = "",
    @SerializedName("amount") var amount: Double = 0.0
): Parcelable