package co.tamara.sdk

import android.content.Intent
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Result object hold status of information via Tamara
 */
@Parcelize
class InformationResult(var status: Int,
                        var error: Throwable? = null): Parcelable {
    companion object{
        /**
         * successfully
         */
        const val STATUS_SUCCESS = 100
        /**
         * failure
         */
        const val STATUS_FAILURE = 101

        /**
         * Creates a Intent contains STATUS_SUCCESS
         */
        internal fun successIntent(extra: String): Intent {
            var intent = Intent()
            intent.putExtra(extra, InformationResult(STATUS_SUCCESS))
            return intent
        }

        /**
         * Creates a Intent contains STATUS_FAILURE and Throwable error
         */
        internal fun failIntent(extra: String, error: Throwable): Intent {
            var intent = Intent()
            intent.putExtra(extra, InformationResult(STATUS_FAILURE, error))
            return intent
        }
    }

    fun hasError(): Boolean {
        return status == STATUS_FAILURE
    }

    fun getMessage(): String? {
        return error?.message
    }

}