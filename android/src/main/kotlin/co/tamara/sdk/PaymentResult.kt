package co.tamara.sdk

import android.content.Intent
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Result object hold status of payment via Tamara Pay
 */
@Parcelize
class PaymentResult(var status: Int,
                    var error: Throwable? = null): Parcelable {
    companion object{
        /**
         * User has cancelled payment
         */
        const val STATUS_CANCEL = 100
        /**
         * Payment has been made successfully
         */
        const val STATUS_SUCCESS = 101
        /**
         * Payment has occurred a failure
         */
        const val STATUS_FAILURE = 102

        /**
         * Creates a Intent contains STATUS_SUCCESS
         */
        internal fun successIntent(extra: String): Intent {
            var intent = Intent()
            intent.putExtra(extra, PaymentResult(STATUS_SUCCESS))
            return intent
        }

        /**
         * Creates a Intent contains STATUS_FAILURE and Throwable error
         */
        internal fun failIntent(extra: String, error: Throwable): Intent {
            var intent = Intent()
            intent.putExtra(extra, PaymentResult(STATUS_FAILURE, error))
            return intent
        }

        /**
         * Creates a Intent contains STATUS_CANCEL
         */
        internal fun cancelIntent(extra: String): Intent {
            var intent = Intent()
            intent.putExtra(extra, PaymentResult(STATUS_CANCEL))
            return intent
        }
    }

    fun hasError(): Boolean {
        return status == STATUS_FAILURE
    }

    fun isCancelled(): Boolean {
        return status == STATUS_CANCEL
    }

    fun getMessage(): String? {
        return error?.message
    }

}