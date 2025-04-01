package co.tamara.sdk

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import co.tamara.sdk.const.PaymentStatus
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.ui.TamaraPaymentFragment

internal class TamaraPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIHelper.initAppComponent()
        setContentView(R.layout.tamara_activity_payment)
        intent?.let {
            var order : Order? = it.getParcelableExtra(EXTRA_ORDER)
            var session : CheckoutSession? = it.getParcelableExtra(EXTRA_CHECKOUT_SESSION)
            val navController = findNavController(this, R.id.sdkNavHostFragment)
            val bundle = Bundle()
            order?.let {
                bundle.putParcelable(TamaraPaymentFragment.ARG_ORDER, order)
            }
            session?.let {
                bundle.putParcelable(TamaraPaymentFragment.ARG_CHECKOUT_SESSION, session)
            }
            bundle.putString(TamaraPaymentFragment.ARG_PAYMENT_STATUS, PaymentStatus.STATUS_INITIALIZE.name)
            navController.setGraph(navController.graph,bundle)
        }
    }

    companion object{
        private const val EXTRA_ORDER = "extra_order"
        private const val EXTRA_CHECKOUT_SESSION = "extra_session"
        const val EXTRA_RESULT = "payment_result"
        fun start(activity: Activity, order: Order, requestCode: Int){
            var intent = Intent(activity, TamaraPaymentActivity::class.java)
            intent.putExtra(EXTRA_ORDER, order)
            activity.startActivityForResult(intent, requestCode)
        }

        fun start(activity: Activity, checkOutUrl: String , requestCode: Int){
            var intent = Intent(activity, TamaraPaymentActivity::class.java)
            intent.putExtra(EXTRA_CHECKOUT_SESSION, CheckoutSession(checkOutUrl, ""))
            activity.startActivityForResult(intent, requestCode)
        }

        fun start(fragment: Fragment, order: Order, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraPaymentActivity::class.java)
            intent.putExtra(EXTRA_ORDER, order)
            fragment.startActivityForResult(intent, requestCode)
        }

        fun start(fragment: Fragment, checkOutUrl: String , requestCode: Int){
            var intent = Intent(fragment.activity, TamaraPaymentActivity::class.java)
            intent.putExtra(EXTRA_CHECKOUT_SESSION, CheckoutSession(checkOutUrl, ""))
            fragment.startActivityForResult(intent, requestCode)
        }
    }
}
