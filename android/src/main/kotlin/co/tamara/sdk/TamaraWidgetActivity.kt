package co.tamara.sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import co.tamara.sdk.const.PaymentStatus
import co.tamara.sdk.model.Order
import co.tamara.sdk.model.request.WidgetProperties
import co.tamara.sdk.model.response.CheckoutSession
import co.tamara.sdk.ui.TamaraPaymentFragment
import co.tamara.sdk.ui.TamaraWidgetFragment

class TamaraWidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIHelper.initAppComponent()
        setContentView(R.layout.tamara_activity_widget)
        intent?.let {
            var properties : WidgetProperties? = it.getParcelableExtra(EXTRA_PROPERTIES)
            val navController = Navigation.findNavController(this, R.id.widgetNavHostFragment)
            val bundle = Bundle()
            properties?.let {
                bundle.putParcelable(TamaraWidgetFragment.ARG_PROPERTIES, properties)
            }
            bundle.putString(TamaraWidgetFragment.ARG_TYPE, it.getStringExtra(EXTRA_TYPE))
            navController.setGraph(navController.graph,bundle)
        }
    }

    companion object{
        private const val EXTRA_PROPERTIES = "extra_properties"
        private const val EXTRA_TYPE = "extra_type"

        fun renderWidgetCartPage(activity: Activity, language: String, country: String, publicKey: String,
                                 amount: Double, requestCode: Int){
            var intent = Intent(activity, TamaraWidgetActivity::class.java)
            intent.putExtra(EXTRA_PROPERTIES, WidgetProperties(language, country, publicKey, amount))
            intent.putExtra(EXTRA_TYPE, "CART")
            activity.startActivityForResult(intent, requestCode)
        }

        fun renderWidgetCartPage(fragment: Fragment, language: String, country: String, publicKey: String,
                                 amount: Double, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraWidgetActivity::class.java)
            intent.putExtra(EXTRA_PROPERTIES, WidgetProperties(language, country, publicKey, amount))
            intent.putExtra(EXTRA_TYPE, "CART")
            fragment.startActivityForResult(intent, requestCode)
        }

        fun renderWidgetProduct(activity: Activity, language: String, country: String, publicKey: String,
                                 amount: Double, requestCode: Int){
            var intent = Intent(activity, TamaraWidgetActivity::class.java)
            intent.putExtra(EXTRA_PROPERTIES, WidgetProperties(language, country, publicKey, amount))
            intent.putExtra(EXTRA_TYPE, "PRODUCT")
            activity.startActivityForResult(intent, requestCode)
        }

        fun renderWidgetProduct(fragment: Fragment, language: String, country: String, publicKey: String,
                                 amount: Double, requestCode: Int){
            var intent = Intent(fragment.activity, TamaraWidgetActivity::class.java)
            intent.putExtra(EXTRA_PROPERTIES, WidgetProperties(language, country, publicKey, amount))
            intent.putExtra(EXTRA_TYPE, "PRODUCT")
            fragment.startActivityForResult(intent, requestCode)
        }
    }
}