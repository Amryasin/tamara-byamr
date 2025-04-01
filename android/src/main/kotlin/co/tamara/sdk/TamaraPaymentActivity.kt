package co.tamara.sdk

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import co.tamara.sdk.const.PaymentStatus
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.ui.TamaraPaymentFragment

internal class TamaraPaymentActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIHelper.initAppComponent()
        setContentView(R.layout.tamara_activity_payment)
        intent?.let {
            val order : Order? = it.getParcelableExtra(EXTRA_ORDER)
            val bundle = Bundle().apply {
                order?.let {
                    putParcelable(TamaraPaymentFragment.ARG_ORDER, order)
                }
                putString(TamaraPaymentFragment.ARG_PAYMENT_STATUS, PaymentStatus.STATUS_INITIALIZE.name)
            }

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.sdkNavHostFragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.tamaraPaymentFragment, bundle)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object{
        private const val EXTRA_ORDER = "extra_order"
        const val EXTRA_RESULT = "payment_result"
        fun start(activity: Activity, order: Order, requestCode: Int){
            val intent = Intent(activity, TamaraPaymentActivity::class.java)
            intent.putExtra(EXTRA_ORDER, order)
            activity.startActivityForResult(intent, requestCode)
        }

        fun start(fragment: Fragment, order: Order, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraPaymentActivity::class.java)
            intent.putExtra(EXTRA_ORDER, order)
            fragment.startActivityForResult(intent, requestCode)
        }
    }
}