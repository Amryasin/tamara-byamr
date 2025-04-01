package co.tamara.sdk

import android.app.Activity
import android.content.Intent
import co.tamara.sdk.model.response.*
import co.tamara.sdk.model.response.orderdetail.OrderDetail

/**
 * Helper class to handle data returned from Tamara SDK
 */
class TamaraInformationHelper {
    companion object{

        /**
         * Tell activity if it should handle in {@link android.app.Activity#onActivityResult onActivityResult}
         * @param requestCode
         * @param resultCode
         * @param data
         */
        fun shouldHandleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
            if(Activity.RESULT_OK == resultCode && data != null){
                return true
            }
            return false
        }

        /**
         * Utility function to get {@link co.tamara.sdk.InformationResult} from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getData(data: Intent): InformationResult? {
            return data.getParcelableExtra("INFORMATION_RESULT")
        }

        /**
         * Utility function to get OrderDetail from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getDataOrderDetail(data: Intent): OrderDetail? {
            return data.getParcelableExtra("ORDER_DETAIL")
        }

        /**
         * Utility function to get OrderDetail from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getDataCapturePayment(data: Intent): CapturePayment? {
            return data.getParcelableExtra("CAPTURE_PAYMENT")
        }

        /**
         * Utility function to get Refunds from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getDataRefunds(data: Intent): RefundsResponse? {
            return data.getParcelableExtra("REFUNDS")
        }

        /**
         * Utility function to get reference from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getOrderReference(data: Intent): OrderReference? {
            return data.getParcelableExtra("REFERENCE")
        }

        /**
         * Utility function to get cart page from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getCartPage(data: Intent): CartPage? {
            return data.getParcelableExtra("CART")
        }

        /**
         * Utility function to get product from intent in onActivityResult
         * @param data intent from onActivityResult
         */
        fun getProduct(data: Intent): Product? {
            return data.getParcelableExtra("PRODUCT")
        }

        fun getCancelOrder(data: Intent): CancelOrderResponse? {
            return data.getParcelableExtra("CANCEL")
        }

        fun getAuthoriseOrder(data: Intent): AuthoriseOrder? {
            return data.getParcelableExtra("AUTHORISE")
        }

        fun getPaymentOptions(data: Intent): PaymentOptionsResponse? {
            return data.getParcelableExtra("PAYMENT_OPTIONS")
        }
    }
}