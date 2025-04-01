package co.tamara.sdk

import android.app.Activity
import android.content.Intent
import co.tamara.sdk.TamaraPayment.Companion.REQUEST_TAMARA_PAYMENT
import co.tamara.sdk.TamaraPaymentActivity.Companion.EXTRA_RESULT
import co.tamara.sdk.model.response.CheckoutSession

/**
 * Helper class to handle data returned from Tamara SDK
 */
class TamaraPaymentHelper {
    companion object{

        /**
         * Tell activity if it should handle in {@link android.app.Activity#onActivityResult onActivityResult}
         * @param requestCode
         * @param resultCode
         * @param data
         */
        fun shouldHandleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
            if(Activity.RESULT_OK == resultCode && REQUEST_TAMARA_PAYMENT == requestCode && data != null && data.hasExtra(
                    EXTRA_RESULT)){
                return true
            }
            return false
        }

        /**
         * Utility function to get {@link co.tamara.sdk.PaymentResult} from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getData(data: Intent): PaymentResult? {
            return data.getParcelableExtra(EXTRA_RESULT)
        }

        fun checkOutSession(data: Intent): CheckoutSession? {
            return data.getParcelableExtra("CHECK_OUT_SESSION")
        }
    }
}