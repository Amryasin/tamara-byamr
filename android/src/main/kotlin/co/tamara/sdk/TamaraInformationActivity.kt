package co.tamara.sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import co.tamara.sdk.const.Information
import co.tamara.sdk.model.request.CancelOrder
import co.tamara.sdk.model.request.CapturePaymentRequest
import co.tamara.sdk.model.request.OrderReference
import co.tamara.sdk.model.request.PaymentOptions
import co.tamara.sdk.model.request.PaymentRefund
import co.tamara.sdk.ui.TamaraInformationFragment

internal class TamaraInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIHelper.initAppComponent()
        setContentView(R.layout.tamara_activity_information)
        intent?.let {
            val navController = findNavController(this, R.id.informationNavHostFragment)
            val bundle = Bundle()
            var orderId : String? = it.getStringExtra(EXTRA_DETAIL)
            var type : String? = it.getStringExtra(EXTRA_TYPE)
            var capturePayment : CapturePaymentRequest? = it.getParcelableExtra(EXTRA_CAPTURE)
            var refunds : PaymentRefund? = it.getParcelableExtra(EXTRA_REFUNDS)
            var orderReference : OrderReference? = it.getParcelableExtra(EXTRA_REFERENCE)
            var cancelOrder : CancelOrder? = it.getParcelableExtra(EXTRA_CANCEL)
            var paymentOptions : PaymentOptions? = it.getParcelableExtra(EXTRA_PAYMENT_OPTIONS)
            orderId?.let {
                bundle.putString(TamaraInformationFragment.ARG_ORDER_ID, it)
            }

            capturePayment?.let {
                bundle.putParcelable(TamaraInformationFragment.ARG_CAPTURE, capturePayment)
            }

            refunds?.let {
                bundle.putParcelable(TamaraInformationFragment.ARG_REFUNDS, refunds)
            }

            orderReference?.let {
                bundle.putParcelable(TamaraInformationFragment.ARG_REFERENCE, orderReference)
            }

            cancelOrder?.let {
                bundle.putParcelable(TamaraInformationFragment.ARG_CANCEL, cancelOrder)
            }
            paymentOptions?.let {
                bundle.putParcelable(TamaraInformationFragment.ARG_PAYMENT_OPTIONS, paymentOptions)
            }
            bundle.putString(TamaraInformationFragment.ARG_TYPE, type.toString())

            navController.setGraph(navController.graph,bundle)
        }
    }
    companion object {
        private const val EXTRA_DETAIL = "extra_detail"
        private const val EXTRA_CAPTURE = "extra_capture"
        private const val EXTRA_REFUNDS = "extra_refunds"
        private const val EXTRA_REFERENCE = "extra_reference"
        private const val EXTRA_CANCEL = "extra_cancel"
        private const val EXTRA_AUTHORISE = "extra_authorise"
        private const val EXTRA_PAYMENT_OPTIONS = "extra_payment_options"
        private const val EXTRA_TYPE = "extra_type"
        fun getOrderDetail(activity: Activity, orderId: String, requestCode: Int){
            var intent = Intent(activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.ORDER_DETAIL.toString())
            activity.startActivityForResult(intent, requestCode)
        }

        fun getOrderDetail(fragment: Fragment, orderId: String, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.ORDER_DETAIL.toString())
            fragment.startActivityForResult(intent, requestCode)
        }

        fun getCapturePayment(activity: Activity, capturePayment: CapturePaymentRequest?, requestCode: Int){
            var intent = Intent(activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_CAPTURE, capturePayment)
            intent.putExtra(EXTRA_TYPE, Information.CAPTURE_PAYMENT.toString())
            activity.startActivityForResult(intent, requestCode)
        }

        fun getCapturePayment(fragment: Fragment, capturePayment: CapturePaymentRequest?, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_CAPTURE, capturePayment)
            intent.putExtra(EXTRA_TYPE, Information.CAPTURE_PAYMENT.toString())
            fragment.startActivityForResult(intent, requestCode)
        }

        fun refunds(activity: Activity, orderId: String, paymentRefund: PaymentRefund?, requestCode: Int){
            var intent = Intent(activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_REFUNDS, paymentRefund)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.REFUNDS.toString())
            activity.startActivityForResult(intent, requestCode)
        }

        fun refunds(fragment: Fragment, orderId: String, paymentRefund: PaymentRefund?, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_REFUNDS, paymentRefund)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.REFUNDS.toString())
            fragment.startActivityForResult(intent, requestCode)
        }

        fun updateOrderReference(activity: Activity, orderId: String, orderReferenceId: String, requestCode: Int){
            var intent = Intent(activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_REFERENCE, OrderReference(orderReferenceId))
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.REFERENCE.toString())
            activity.startActivityForResult(intent, requestCode)
        }

        fun updateOrderReference(fragment: Fragment, orderId: String, orderReferenceId: String, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_REFERENCE, OrderReference(orderReferenceId))
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.REFERENCE.toString())
            fragment.startActivityForResult(intent, requestCode)
        }

        fun cancelOrder(activity: Activity, orderId: String, cancelOrder: CancelOrder?, requestCode: Int){
            var intent = Intent(activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_CANCEL, cancelOrder)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.CANCEL.toString())
            activity.startActivityForResult(intent, requestCode)
        }

        fun cancelOrder(fragment: Fragment, orderId: String, cancelOrder: CancelOrder?, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_CANCEL, cancelOrder)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.CANCEL.toString())
            fragment.startActivityForResult(intent, requestCode)
        }

        fun authoriseOrder(activity: Activity, orderId: String, requestCode: Int){
            var intent = Intent(activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.AUTHORISE.toString())
            activity.startActivityForResult(intent, requestCode)
        }

        fun authoriseOrder(fragment: Fragment, orderId: String, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_DETAIL, orderId)
            intent.putExtra(EXTRA_TYPE, Information.AUTHORISE.toString())
            fragment.startActivityForResult(intent, requestCode)
        }

        fun paymentOptions(activity: Activity, paymentOptions: PaymentOptions?, requestCode: Int){
            var intent = Intent(activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_PAYMENT_OPTIONS, paymentOptions)
            intent.putExtra(EXTRA_TYPE, Information.PAYMENT_OPTIONS.toString())
            activity.startActivityForResult(intent, requestCode)
        }

        fun paymentOptions(fragment: Fragment, paymentOptions: PaymentOptions?, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraInformationActivity::class.java)
            intent.putExtra(EXTRA_PAYMENT_OPTIONS, paymentOptions)
            intent.putExtra(EXTRA_TYPE, Information.PAYMENT_OPTIONS.toString())
            fragment.startActivityForResult(intent, requestCode)
        }
    }
}